{
    "code": 20000,
    "message": null,
    "data": {
    //核心mcmf可视化描述的入口
        "dm": {
        //表示mcmf的节点
            "data": [
            //表示第0个节点，是开始节点
                {
                    "name": "0",//代表索引，不能重复
                    "itemStyle": {//表示节点的颜色，用于区分开始、结束节点和中间的应用、任务和主机节点，共计5类
                        "color": "#ff9999"
                    },
                    //x和y分别表示节点的位置，相较于左上角的像素值，需要根据某一列节点的数量分别创建
                    "x": 100,
                    "y": 200,
                    //表示点的大小
                    "symbolSize": 50,
                    //鼠标放上去显示的内容,需要指定value
                    "tooltip":{
                        "formatter":"value"
                    }
                },
                {
                    "name": "11",
                    "itemStyle": {
                        "color": "#0399d3"
                    },
                    "x": 500,
                    "y": 200,
                    "symbolSize": 50
                },
                {
                    "name": "6",
                    "itemStyle": {
                        "color": "SandyBrown"
                    },
                    "x": 300,
                    "y": 133,
                    "symbolSize": 50
                },
                {
                    "name": "7",
                    "itemStyle": {
                        "color": "SandyBrown"
                    },
                    "x": 300,
                    "y": 266,
                    "symbolSize": 50
                },
                {
                    "name": "8",
                    "itemStyle": {
                        "color": "MediumTurquoise"
                    },
                    "x": 400,
                    "y": 100,
                    "symbolSize": 50
                },
                {
                    "name": "9",
                    "itemStyle": {
                        "color": "MediumTurquoise"
                    },
                    "x": 400,
                    "y": 200,
                    "symbolSize": 50
                },
                {
                    "name": "10",
                    "itemStyle": {
                        "color": "MediumTurquoise"
                    },
                    "x": 400,
                    "y": 300,
                    "symbolSize": 50
                },
                {
                    "name": "1",
                    "itemStyle": {
                        "color": "#67b55b"
                    },
                    "x": 200,
                    "y": 66,
                    "symbolSize": 50
                },
                {
                    "name": "2",
                    "itemStyle": {
                        "color": "#67b55b"
                    },
                    "x": 200,
                    "y": 132,
                    "symbolSize": 50
                },
                {
                    "name": "3",
                    "itemStyle": {
                        "color": "#67b55b"
                    },
                    "x": 200,
                    "y": 198,
                    "symbolSize": 50
                },
                {
                    "name": "4",
                    "itemStyle": {
                        "color": "#67b55b"
                    },
                    "x": 200,
                    "y": 264,
                    "symbolSize": 50
                },
                {
                    "name": "5",
                    "itemStyle": {
                        "color": "#67b55b"
                    },
                    "x": 200,
                    "y": 330,
                    "symbolSize": 50
                }
            ],
            //表示mcmf中所有的边
            "links": [
                {
                //每一个边的具体描述
                    "source": "8",//代表节点的index，通过name描述，边的起始位置
                    "target": "11",//代表边的结束位置
                    "label": {//边展示的样式，一般不用改
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,0>",
                            "curveness": 0,
                            "color": "#000"//只需要调整颜色
                        }
                    },
                    "lineStyle": {//边展示的样式，一般与label保持一致
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [//具体的展示尺寸，不需要调整
                        5,
                        10
                    ]
                },
                {
                    "source": "9",
                    "target": "11",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,0>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "10",
                    "target": "11",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,0>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "0",
                    "target": "1",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<4,10>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "1",
                    "target": "6",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<4,10>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "0",
                    "target": "2",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<2,5>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "2",
                    "target": "6",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<2,5>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "0",
                    "target": "3",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<2,3>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "3",
                    "target": "6",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<2,3>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "0",
                    "target": "4",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<3,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "4",
                    "target": "7",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<3,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "0",
                    "target": "5",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<5,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "5",
                    "target": "7",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<5,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "6",
                    "target": "8",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "6",
                    "target": "9",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "6",
                    "target": "10",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "7",
                    "target": "8",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "7",
                    "target": "9",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                },
                {
                    "source": "7",
                    "target": "10",
                    "label": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "<8,1>",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "lineStyle": {
                        "normal": {
                            "show": true,
                            "width": 4,
                            "formatter": "",
                            "curveness": 0,
                            "color": "#000"
                        }
                    },
                    "symbolSize": [
                        5,
                        10
                    ]
                }
            ],
            //具体动画的控制位置，按照顺序依次执行
            "animations": [
                //如第一个动画，更改了0,4边上的线条颜色
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "4",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "4",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<3,1>",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "4",
                        "target": "7",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "4",
                        "target": "7",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<3,1>",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "7",
                        "target": "8",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<5,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "7",
                        "target": "8",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<8,1>",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "8",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<5,0>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "8",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<5,0>",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#353C81"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                //如以下动画，更改了节点4的大小，先放大
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "4",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                //如以下动画，更改了节点4的大小，再缩小
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "4",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "8",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "8",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "4",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "4",
                        "target": "7",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Task4",
                        "itemStyle": {
                            "color": "#67b55b"
                        },
                        "x": 600,
                        "y": 66,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Mach8",
                        "itemStyle": {
                            "color": "MediumTurquoise"
                        },
                        "x": 700,
                        "y": 66,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "add",
                    "link": {
                        "source": "Task4",
                        "target": "Mach8",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "deploy on",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "5",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "5",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<5,1>",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "5",
                        "target": "7",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "5",
                        "target": "7",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<5,1>",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "7",
                        "target": "8",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "7",
                        "target": "8",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<8,1>",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "8",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,0>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "8",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,0>",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#DE4C9E"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "5",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "5",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "8",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "8",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "5",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "5",
                        "target": "7",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Task5",
                        "itemStyle": {
                            "color": "#67b55b"
                        },
                        "x": 600,
                        "y": 132,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "add",
                    "link": {
                        "source": "Task5",
                        "target": "Mach8",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "deploy on",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "3",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,3>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "3",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<2,3>",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "3",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,3>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "3",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<2,3>",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "6",
                        "target": "9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<6,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "6",
                        "target": "9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<8,1>",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "9",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<6,0>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "9",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<6,0>",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#882D9C"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "3",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "3",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "9",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "9",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "3",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,3>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "3",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,3>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                //如以下是三个动画，更新了三个元素，代表了一次放置结果
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Task3",
                        "itemStyle": {
                            "color": "#67b55b"
                        },
                        "x": 600,
                        "y": 198,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Mach9",
                        "itemStyle": {
                            "color": "MediumTurquoise"
                        },
                        "x": 700,
                        "y": 198,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "add",
                    "link": {
                        "source": "Task3",
                        "target": "Mach9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "deploy on",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "2",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,5>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "2",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<2,5>",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "2",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,5>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "2",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<2,5>",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "6",
                        "target": "9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<4,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "6",
                        "target": "9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<8,1>",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "9",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<4,0>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "9",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<4,0>",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#147624"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "2",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "2",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "9",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "9",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "2",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,5>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "2",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,5>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Task2",
                        "itemStyle": {
                            "color": "#67b55b"
                        },
                        "x": 600,
                        "y": 264,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "add",
                    "link": {
                        "source": "Task2",
                        "target": "Mach9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "deploy on",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "1",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,10>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "1",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<4,10>",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "1",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,10>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "1",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<4,10>",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "6",
                        "target": "9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,1>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "6",
                        "target": "9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<8,1>",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "9",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,0>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "9",
                        "target": "11",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,0>",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#78BBCB"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "1",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "1",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "9",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 70
                    }
                },
                {
                    "type": "data",
                    "action": "reset",
                    "link": null,
                    "data": {
                        "name": "9",
                        "itemStyle": {},
                        "x": 0,
                        "y": 0,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "0",
                        "target": "1",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,10>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "link",
                    "action": "reset",
                    "link": {
                        "source": "1",
                        "target": "6",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "<0,10>",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#FFF"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                },
                {
                    "type": "data",
                    "action": "add",
                    "link": null,
                    "data": {
                        "name": "Task1",
                        "itemStyle": {
                            "color": "#67b55b"
                        },
                        "x": 600,
                        "y": 330,
                        "symbolSize": 50
                    }
                },
                {
                    "type": "link",
                    "action": "add",
                    "link": {
                        "source": "Task1",
                        "target": "Mach9",
                        "label": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "deploy on",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "lineStyle": {
                            "normal": {
                                "show": true,
                                "width": 4,
                                "formatter": "",
                                "curveness": 0,
                                "color": "#000"
                            }
                        },
                        "symbolSize": [
                            5,
                            10
                        ]
                    },
                    "data": null
                }
            ],
            "colors": [
                "#ff9999",
                "#67b55b",
                "SandyBrown",
                "MediumTurquoise",
                "#0399d3"
            ],
            "categories": [
                {
                    "name": "起点"
                },
                {
                    "name": "任务(Task)"
                },
                {
                    "name": "应用"
                },
                {
                    "name": "主机(Mach)"
                },
                {
                    "name": "终点"
                }
            ]
        },
        "n": 12,
        "s": 0,
        "t": 11,
        "maxFlow": 16,
        "minCost": 144,
        "minCut": [
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
        ],
        "graph": [
            [
                {
                    "from": 0,
                    "to": 1,
                    "residual": false,
                    "flow": 4,
                    "cost": 10,
                    "capacity": 4,
                    "originalCost": 10
                },
                {
                    "from": 0,
                    "to": 2,
                    "residual": false,
                    "flow": 2,
                    "cost": 5,
                    "capacity": 2,
                    "originalCost": 5
                },
                {
                    "from": 0,
                    "to": 3,
                    "residual": false,
                    "flow": 2,
                    "cost": 3,
                    "capacity": 2,
                    "originalCost": 3
                },
                {
                    "from": 0,
                    "to": 4,
                    "residual": false,
                    "flow": 3,
                    "cost": 1,
                    "capacity": 3,
                    "originalCost": 1
                },
                {
                    "from": 0,
                    "to": 5,
                    "residual": false,
                    "flow": 5,
                    "cost": 1,
                    "capacity": 5,
                    "originalCost": 1
                }
            ],
            [
                {
                    "from": 1,
                    "to": 0,
                    "residual": true,
                    "flow": -4,
                    "cost": -10,
                    "capacity": 0,
                    "originalCost": -10
                },
                {
                    "from": 1,
                    "to": 6,
                    "residual": false,
                    "flow": 4,
                    "cost": 10,
                    "capacity": 4,
                    "originalCost": 10
                }
            ],
            [
                {
                    "from": 2,
                    "to": 0,
                    "residual": true,
                    "flow": -2,
                    "cost": -5,
                    "capacity": 0,
                    "originalCost": -5
                },
                {
                    "from": 2,
                    "to": 6,
                    "residual": false,
                    "flow": 2,
                    "cost": 5,
                    "capacity": 2,
                    "originalCost": 5
                }
            ],
            [
                {
                    "from": 3,
                    "to": 0,
                    "residual": true,
                    "flow": -2,
                    "cost": -3,
                    "capacity": 0,
                    "originalCost": -3
                },
                {
                    "from": 3,
                    "to": 6,
                    "residual": false,
                    "flow": 2,
                    "cost": 3,
                    "capacity": 2,
                    "originalCost": 3
                }
            ],
            [
                {
                    "from": 4,
                    "to": 0,
                    "residual": true,
                    "flow": -3,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 4,
                    "to": 7,
                    "residual": false,
                    "flow": 3,
                    "cost": 1,
                    "capacity": 3,
                    "originalCost": 1
                }
            ],
            [
                {
                    "from": 5,
                    "to": 0,
                    "residual": true,
                    "flow": -5,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 5,
                    "to": 7,
                    "residual": false,
                    "flow": 5,
                    "cost": 1,
                    "capacity": 5,
                    "originalCost": 1
                }
            ],
            [
                {
                    "from": 6,
                    "to": 1,
                    "residual": true,
                    "flow": -4,
                    "cost": -10,
                    "capacity": 0,
                    "originalCost": -10
                },
                {
                    "from": 6,
                    "to": 2,
                    "residual": true,
                    "flow": -2,
                    "cost": -5,
                    "capacity": 0,
                    "originalCost": -5
                },
                {
                    "from": 6,
                    "to": 3,
                    "residual": true,
                    "flow": -2,
                    "cost": -3,
                    "capacity": 0,
                    "originalCost": -3
                },
                {
                    "from": 6,
                    "to": 8,
                    "residual": false,
                    "flow": 0,
                    "cost": 1,
                    "capacity": 8,
                    "originalCost": 1
                },
                {
                    "from": 6,
                    "to": 9,
                    "residual": false,
                    "flow": 8,
                    "cost": 1,
                    "capacity": 8,
                    "originalCost": 1
                },
                {
                    "from": 6,
                    "to": 10,
                    "residual": false,
                    "flow": 0,
                    "cost": 1,
                    "capacity": 8,
                    "originalCost": 1
                }
            ],
            [
                {
                    "from": 7,
                    "to": 4,
                    "residual": true,
                    "flow": -3,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 7,
                    "to": 5,
                    "residual": true,
                    "flow": -5,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 7,
                    "to": 8,
                    "residual": false,
                    "flow": 8,
                    "cost": 1,
                    "capacity": 8,
                    "originalCost": 1
                },
                {
                    "from": 7,
                    "to": 9,
                    "residual": false,
                    "flow": 0,
                    "cost": 1,
                    "capacity": 8,
                    "originalCost": 1
                },
                {
                    "from": 7,
                    "to": 10,
                    "residual": false,
                    "flow": 0,
                    "cost": 1,
                    "capacity": 8,
                    "originalCost": 1
                }
            ],
            [
                {
                    "from": 8,
                    "to": 11,
                    "residual": false,
                    "flow": 8,
                    "cost": 0,
                    "capacity": 8,
                    "originalCost": 0
                },
                {
                    "from": 8,
                    "to": 6,
                    "residual": true,
                    "flow": 0,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 8,
                    "to": 7,
                    "residual": true,
                    "flow": -8,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                }
            ],
            [
                {
                    "from": 9,
                    "to": 11,
                    "residual": false,
                    "flow": 8,
                    "cost": 0,
                    "capacity": 8,
                    "originalCost": 0
                },
                {
                    "from": 9,
                    "to": 6,
                    "residual": true,
                    "flow": -8,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 9,
                    "to": 7,
                    "residual": true,
                    "flow": 0,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                }
            ],
            [
                {
                    "from": 10,
                    "to": 11,
                    "residual": false,
                    "flow": 0,
                    "cost": 0,
                    "capacity": 8,
                    "originalCost": 0
                },
                {
                    "from": 10,
                    "to": 6,
                    "residual": true,
                    "flow": 0,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                },
                {
                    "from": 10,
                    "to": 7,
                    "residual": true,
                    "flow": 0,
                    "cost": -1,
                    "capacity": 0,
                    "originalCost": -1
                }
            ],
            [
                {
                    "from": 11,
                    "to": 8,
                    "residual": true,
                    "flow": -8,
                    "cost": 0,
                    "capacity": 0,
                    "originalCost": 0
                },
                {
                    "from": 11,
                    "to": 9,
                    "residual": true,
                    "flow": -8,
                    "cost": 0,
                    "capacity": 0,
                    "originalCost": 0
                },
                {
                    "from": 11,
                    "to": 10,
                    "residual": true,
                    "flow": 0,
                    "cost": 0,
                    "capacity": 0,
                    "originalCost": 0
                }
            ]
        ],
        "visitedToken": 1,
        "visited": [
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        ],
        "solved": true
    }
}