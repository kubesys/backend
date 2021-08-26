package com.github.kubesys.mcmf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.kubeclient.KubernetesClient;

public class GenerateRequestTest {

	static Map<String, Integer> mapper = new HashMap<>();

	public static void main(String[] args) throws Exception {

		KubernetesClient client = new KubernetesClient("https://124.70.64.232:6443",
				"");

		ObjectNode json = new ObjectMapper().createObjectNode();
		
		int num = initPods(client, json);
		initApps(json);
		
		initMachines(client, json, num);
		
		
		System.out.println(json.toPrettyString());
	}

	private static void initMachines(KubernetesClient client, ObjectNode json, int num) throws Exception {
		int id = num + mapper.keySet().size() + 1;
		JsonNode nodes = client.listResources("Node").get("items");
		
		ArrayNode machines = new ObjectMapper().createArrayNode();
		for (int i = 0; i < nodes.size(); i++) {
			ObjectNode machine = new ObjectMapper().createObjectNode();
			machine.put("index", id++);
			machine.put("cost", 1);
			machine.put("name", nodes.get(i).get("metadata").get("name").asText());
			machine.put("cpu", 100);
			machines.add(machine);
		}
		json.set("machines", machines);
	}

	private static void initApps(ObjectNode json) {
		ArrayNode applications = new ObjectMapper().createArrayNode();
		for (String name : mapper.keySet()) {
			ObjectNode application = new ObjectMapper().createObjectNode();
			application.put("index", mapper.get(name));
			application.put("cost", 1);
			application.put("name", name);
			applications.add(application);
		}
		json.set("applications", applications);
	}

	private static int initPods(KubernetesClient client, ObjectNode json) throws Exception {
		List<JsonNode> pods = getPods(client);
		
		ArrayNode tasks = new ObjectMapper().createArrayNode();

		int id = 1;
		
		int aid = pods.size() + 1;

		for (JsonNode pod : pods) {
			ObjectNode task = new ObjectMapper().createObjectNode();
			task.put("index", id++);
			task.put("cpu", 1);
			task.put("cost", 1);
			String name = pod.get("metadata").get("name").asText();
			task.put("name", name);

			if (!pod.get("metadata").has("ownerReferences")) {
				task.put("application", aid);
				mapper.put("pod-" + name, aid++);
			} else {
				JsonNode owner = pod.get("metadata").get("ownerReferences").get(0);
				String nname = owner.get("kind").asText().toLowerCase() + "-"
						+ owner.get("name").asText().toLowerCase();
				if (mapper.get(nname) == null) {
					task.put("application", aid);
					mapper.put(nname, aid++);
				} else {
					task.put("application", mapper.get(nname));
				}
			}

			tasks.add(task);
		}

		json.set("tasks", tasks);
		return pods.size();
	}

	static List<JsonNode> getPods(KubernetesClient client) throws Exception {
		List<JsonNode> list = new ArrayList<>();
		JsonNode pods = client.listResources("Pod").get("items");
		for (int i = 0; i < pods.size(); i++) {
			JsonNode pod = pods.get(i);
			if (pod.get("metadata").get("namespace").asText().equals("kube-system")) {
				continue;
			}
			list.add(pod);
		}
		return list;
	}
}
