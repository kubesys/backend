package com.github.kubesys.mcmf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.kubeclient.KubernetesClient;

public class OldMockUtil {

	static Map<String, Integer> podToIdMapper = new HashMap<>();

	static Map<String, Integer> appToIdMapper = new HashMap<>();

	static Map<String, Integer> machineToIdMapper = new HashMap<>();

	static Map<String, String> podToAppMapper = new HashMap<>();

	static Map<String, String> podToMachineMapper = new HashMap<>();

	static Set<String> machineSet = new HashSet<>();

	static String color = "[\n" + "\t\t\t\"#ff9999\",\n" + "\t\t\t\"#67b55b\",\n" + "\t\t\t\"SandyBrown\",\n"
			+ "\t\t\t\"MediumTurquoise\",\n" + "\t\t\t\"#0399d3\"\n" + "\t\t\t]";

	static String categories = "[\n" + "                {\n" + "                    \"name\": \"起点\"\n"
			+ "                },\n" + "                {\n" + "                    \"name\": \"任务(Task)\"\n"
			+ "                },\n" + "                {\n" + "                    \"name\": \"应用\"\n"
			+ "                },\n" + "                {\n" + "                    \"name\": \"主机(Mach)\"\n"
			+ "                },\n" + "                {\n" + "                    \"name\": \"终点\"\n"
			+ "                }\n" + "            ]";

	public static void main(String[] args) throws Exception {

		KubernetesClient client = new KubernetesClient("", "");

		ObjectNode dm = new ObjectMapper().createObjectNode();

		dm.set("data", initData(client));

		dm.set("link", initLink());

		dm.set("animations", initAnimations());

		dm.set("colors", (new ObjectMapper()).readTree(color));

		dm.set("categories", (new ObjectMapper()).readTree(categories));

		System.out.println(dm.toPrettyString());
	}

	public static JsonNode getJson() throws Exception {
		KubernetesClient client = new KubernetesClient("", "");

		ObjectNode json = new ObjectMapper().createObjectNode();
		ObjectNode dm = new ObjectMapper().createObjectNode();

		dm.set("data", initData(client));

		dm.set("links", initLink());

		dm.set("animations", initAnimations());

		dm.set("colors", (new ObjectMapper()).readTree(color));

		dm.set("categories", (new ObjectMapper()).readTree(categories));

		json.set("dm", dm);

		return json;
	}

	public static ObjectNode getDataInstance(String id, String color, int x, int y, String name) {
		ObjectNode data = new ObjectMapper().createObjectNode();
		data.put("name", id);

		ObjectNode itemStyle = new ObjectMapper().createObjectNode();
		itemStyle.put("color", color);
		data.set("itemStyle", itemStyle);

		data.put("x", x);
		data.put("y", y);
		data.put("symbolSize", 50);

		ObjectNode tooltip = new ObjectMapper().createObjectNode();
		tooltip.put("formatter", name);
		data.set("tooltip", tooltip);

		return data;
	}

	public static ObjectNode getLinkInstance(String source, String target, String color) {
		ObjectNode link = new ObjectMapper().createObjectNode();
		link.put("source", source);
		link.put("target", target);

		ObjectNode label = new ObjectMapper().createObjectNode();
		ObjectNode normal = new ObjectMapper().createObjectNode();
		normal.put("show", true);
		normal.put("width", 4);
		normal.put("formatter", "");
		normal.put("curveness", 0);
		normal.put("color", color);
		label.set("normal", normal);
		link.set("label", label);

		ObjectNode lineStyle = new ObjectMapper().createObjectNode();
		ObjectNode normal2 = new ObjectMapper().createObjectNode();
		normal2.put("show", true);
		normal2.put("width", 4);
		normal2.put("formatter", "");
		normal2.put("curveness", 0);
		normal2.put("color", color);
		lineStyle.set("normal", normal2);
		link.set("lineStyle", lineStyle);

		ArrayNode symbolSize = new ObjectMapper().createArrayNode();
		symbolSize.add(5);
		symbolSize.add(10);
		link.set("symbolSize", symbolSize);

		return link;
	}

	public static ArrayNode initAnimations() {
		ArrayNode animationSet = new ObjectMapper().createArrayNode();
		int i = 0, j = 0;
		int endIndex = podToIdMapper.size() + appToIdMapper.size() + machineToIdMapper.size() + 1;
		for (String podName : podToMachineMapper.keySet()) {
			ObjectNode animationNode1 = new ObjectMapper().createObjectNode();
			animationNode1.put("type", "link");
			animationNode1.put("action", "reset");
			animationNode1.set("link", getLinkInstance(0 + "", podToIdMapper.get(podName) + "", "#000"));
			animationSet.add(animationNode1);

			ObjectNode animationNode2 = new ObjectMapper().createObjectNode();
			animationNode2.put("type", "link");
			animationNode2.put("action", "reset");
			animationNode2.set("link", getLinkInstance(0 + "", podToIdMapper.get(podName) + "", "#FFF"));
			animationSet.add(animationNode2);

			ObjectNode animationNode3 = new ObjectMapper().createObjectNode();
			animationNode3.put("type", "link");
			animationNode3.put("action", "reset");
			animationNode3.set("link", getLinkInstance(0 + "", podToIdMapper.get(podName) + "", "#000"));
			animationSet.add(animationNode3);

			ObjectNode animationNode22 = new ObjectMapper().createObjectNode();
			animationNode22.put("type", "link");
			animationNode22.put("action", "reset");
			animationNode22.set("link", getLinkInstance(0 + "", podToIdMapper.get(podName) + "", "#FFF"));
			animationSet.add(animationNode22);
			
			ObjectNode animationNode4 = new ObjectMapper().createObjectNode();
			animationNode4.put("type", "link");
			animationNode4.put("action", "reset");
			animationNode4.set("link", getLinkInstance(podToIdMapper.get(podName) + "",
					appToIdMapper.get(podToAppMapper.get(podName)) + "", "#FFF"));
			animationSet.add(animationNode4);

			ObjectNode animationNode5 = new ObjectMapper().createObjectNode();
			animationNode5.put("type", "link");
			animationNode5.put("action", "reset");
			animationNode5.set("link", getLinkInstance(podToIdMapper.get(podName) + "",
					appToIdMapper.get(podToAppMapper.get(podName)) + "", "#000"));
			animationSet.add(animationNode5);
			
			ObjectNode animationNode44 = new ObjectMapper().createObjectNode();
			animationNode44.put("type", "link");
			animationNode44.put("action", "reset");
			animationNode44.set("link", getLinkInstance(podToIdMapper.get(podName) + "",
					appToIdMapper.get(podToAppMapper.get(podName)) + "", "#FFF"));
			animationSet.add(animationNode44);

			ObjectNode animationNode6 = new ObjectMapper().createObjectNode();
			animationNode6.put("type", "link");
			animationNode6.put("action", "reset");
			animationNode6.set("link", getLinkInstance(appToIdMapper.get(podToAppMapper.get(podName)) + "",
					machineToIdMapper.get(podToMachineMapper.get(podName)) + "", "#FFF"));
			animationSet.add(animationNode6);
			

			ObjectNode animationNode7 = new ObjectMapper().createObjectNode();
			animationNode7.put("type", "link");
			animationNode7.put("action", "reset");
			animationNode7.set("link", getLinkInstance(appToIdMapper.get(podToAppMapper.get(podName)) + "",
					machineToIdMapper.get(podToMachineMapper.get(podName)) + "", "#000"));
			animationSet.add(animationNode7);
			
			ObjectNode animationNode66 = new ObjectMapper().createObjectNode();
			animationNode66.put("type", "link");
			animationNode66.put("action", "reset");
			animationNode66.set("link", getLinkInstance(appToIdMapper.get(podToAppMapper.get(podName)) + "",
					machineToIdMapper.get(podToMachineMapper.get(podName)) + "", "#FFF"));
			animationSet.add(animationNode66);

			ObjectNode animationNode8 = new ObjectMapper().createObjectNode();
			animationNode8.put("type", "link");
			animationNode8.put("action", "reset");
			animationNode8.set("link", getLinkInstance(machineToIdMapper.get(podToMachineMapper.get(podName)) + "",
					endIndex + "", "#FFF"));
			animationSet.add(animationNode8);

			ObjectNode animationNode88 = new ObjectMapper().createObjectNode();
			animationNode88.put("type", "link");
			animationNode88.put("action", "reset");
			animationNode88.set("link", getLinkInstance(machineToIdMapper.get(podToMachineMapper.get(podName)) + "",
					endIndex + "", "#000"));
			animationSet.add(animationNode88);
			
			ObjectNode animationNode888 = new ObjectMapper().createObjectNode();
			animationNode888.put("type", "link");
			animationNode888.put("action", "reset");
			animationNode888.set("link", getLinkInstance(machineToIdMapper.get(podToMachineMapper.get(podName)) + "",
					endIndex + "", "#FFF"));
			animationSet.add(animationNode888);

			// String id, String color, int x, int y, String name)

			ObjectNode animationNode9 = new ObjectMapper().createObjectNode();
			animationNode9.put("type", "data");
			animationNode9.put("action", "add");
			animationNode9.set("data",
					getDataInstance("T" + podToIdMapper.get(podName), "#67b55b", 600, 100 + i * 100, podName));
			animationSet.add(animationNode9);
			i++;

			if (!machineSet.contains("M" + machineToIdMapper.get(podToMachineMapper.get(podName)))) {
				ObjectNode animationNode10 = new ObjectMapper().createObjectNode();
				animationNode10.put("type", "data");
				animationNode10.put("action", "add");

				animationNode10.set("data",
						getDataInstance("M" + machineToIdMapper.get(podToMachineMapper.get(podName)),
								"MediumTurquoise", 700, 100 + j * 100, podToMachineMapper.get(podName)));
				animationSet.add(animationNode10);
				j++;
				machineSet.add("M" + machineToIdMapper.get(podToMachineMapper.get(podName)));
			} 

			ObjectNode animationNode11 = new ObjectMapper().createObjectNode();
			animationNode11.put("type", "link");
			animationNode11.put("action", "add");
			animationNode11.set("link", getLinkInstance("T" + podToIdMapper.get(podName),
					"M" + machineToIdMapper.get(podToMachineMapper.get(podName)), "#000"));
			animationSet.add(animationNode11);
		}

		return animationSet;
	}

	public static ArrayNode initLink() {
		ArrayNode linkSet = new ObjectMapper().createArrayNode();

		for (String podName : podToIdMapper.keySet()) {
			initLinkJson(linkSet, 0, podToIdMapper.get(podName));
		}

		for (String podName : podToAppMapper.keySet()) {
			initLinkJson(linkSet, podToIdMapper.get(podName), appToIdMapper.get(podToAppMapper.get(podName)));
		}

		for (String podName : podToMachineMapper.keySet()) {
			initLinkJson(linkSet, appToIdMapper.get(podToAppMapper.get(podName)),
					machineToIdMapper.get(podToMachineMapper.get(podName)));
		}

		for (String machineName : machineToIdMapper.keySet()) {
			initLinkJson(linkSet, machineToIdMapper.get(machineName),
					podToIdMapper.size() + appToIdMapper.size() + machineToIdMapper.size() + 1);
		}
		return linkSet;
	}

	public static ArrayNode initData(KubernetesClient client) throws Exception {
		ArrayNode dataSet = new ObjectMapper().createArrayNode();

		// source
		initDataJson(dataSet, 0, 100, 200, "#ff9999", "source");

		// tasks
		List<JsonNode> pods = getPods(client);
		for (int i = 0; i < pods.size(); i++) {
			JsonNode pod = pods.get(i);
			String name = pod.get("metadata").get("name").asText();
			initRelationShips(pods, pod, name);
			initDataJson(dataSet, i + 1, 200, 66 + i * 66, "#67b55b", name);
			podToIdMapper.put(name, i + 1);
		}

		// apps
		int pos = 0;
		for (String name : appToIdMapper.keySet()) {
			initDataJson(dataSet, appToIdMapper.get(name), 300, 100 + 100 * pos, "SandyBrown", name);
			pos++;
		}

		// machines
		int id = pods.size() + appToIdMapper.keySet().size() + 1;
		JsonNode nodes = client.listResources("Node").get("items");
		for (int i = 0; i < nodes.size(); i++) {
			String name = nodes.get(i).get("metadata").get("name").asText();
			initDataJson(dataSet, id + i, 400, 100 + i * 100, "MediumTurquoise", name);
			machineToIdMapper.put(name, id + i);
		}

		// sink
		initDataJson(dataSet, pods.size() + appToIdMapper.keySet().size() + nodes.size() + 1, 500, 100, "#0399d3",
				"sink");
		return dataSet;
	}

	public static void initRelationShips(List<JsonNode> pods, JsonNode pod, String name) {
		if (pod.get("metadata").has("ownerReferences")) {
			JsonNode owner = pod.get("metadata").get("ownerReferences").get(0);
			String nname = owner.get("kind").asText().toLowerCase() + "-" + owner.get("name").asText().toLowerCase();
			if (appToIdMapper.get(nname) == null) {
				appToIdMapper.put(nname, pods.size() + appToIdMapper.size() + 1);
			}
			podToAppMapper.put(name, nname);
		} else {
			appToIdMapper.put("pod-" + name, pods.size() + appToIdMapper.size() + 1);
			podToAppMapper.put(name, "pod-" + name);
		}

		podToMachineMapper.put(name, pod.get("spec").get("nodeName").asText());
	}

	public static void initDataJson(ArrayNode dataSet, int id, int x, int y, String color, String name) {
		ObjectNode data = new ObjectMapper().createObjectNode();
		data.put("name", id);

		ObjectNode itemStyle = new ObjectMapper().createObjectNode();
		itemStyle.put("color", color);
		data.set("itemStyle", itemStyle);

		data.put("x", x);
		data.put("y", y);
		data.put("symbolSize", 50);

		ObjectNode tooltip = new ObjectMapper().createObjectNode();
		tooltip.put("formatter", name);
		data.set("tooltip", tooltip);

		dataSet.add(data);
	}

	public static void initLinkJson(ArrayNode linkSet, int source, int sink) {
		ObjectNode link = new ObjectMapper().createObjectNode();
		link.put("source", source);
		link.put("target", sink);

		ObjectNode label = new ObjectMapper().createObjectNode();
		ObjectNode normal = new ObjectMapper().createObjectNode();
		normal.put("show", true);
		normal.put("width", 4);
		normal.put("formatter", "");
		normal.put("curveness", 0);
		normal.put("color", "#000");
		label.set("normal", normal);
		link.set("label", label);

		ObjectNode lineStyle = new ObjectMapper().createObjectNode();
		ObjectNode normal2 = new ObjectMapper().createObjectNode();
		normal2.put("show", true);
		normal2.put("width", 4);
		normal2.put("formatter", "");
		normal2.put("curveness", 0);
		normal2.put("color", "#000");
		lineStyle.set("normal", normal2);
		link.set("lineStyle", lineStyle);

		ArrayNode symbolSize = new ObjectMapper().createArrayNode();
		symbolSize.add(5);
		symbolSize.add(10);
		link.set("symbolSize", symbolSize);

		linkSet.add(link);
	}

	public static List<JsonNode> getPods(KubernetesClient client) throws Exception {
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