# 🚀 DisruptorDemo - 协作风暴，极速狂飙！

你好呀，探索者！👋 欢迎来到 **DisruptorDemo**！这里是高性能并发技术的练兵场，我们致力于把“即时协作”这件事做到极致的丝滑与快速！准备好感受代码的“速度与激情”了吗？🏎️💨

## 🌟 项目概述

**DisruptorDemo** 是一个展示如何使用 **LMAX Disruptor** 高性能无锁队列与 **WebSocket** 实时通信技术打造低延迟协同应用的演示项目。

我们的目标很简单：**拒绝卡顿，拒绝冲突！**
想象一下，当几个人同时对一张图片进行旋转、缩放操作时，如何保证指令不打架？后端如何瞬间处理海量并发请求？本项目就是为了回答这些问题而生！我们用 Disruptor 替代了传统的阻塞队列，让消息处理如闪电般迅速。⚡

**核心亮点：**
- **⚡ 唯快不破**：基于 RingBuffer 的无锁设计，吞吐量爆表！
- **🤝 实时同频**：WebSocket 全双工通信，你的操作，队友秒懂。
- **🛡️ 秩序井然**：精心设计的并发控制，让多人协作不再是一团乱麻。

## 🛠️ 安装指南

想要运行这个酷炫的项目？只需要准备好以下装备：

**必备神器：**
1.  **JDK 21**：我们需要最新的 Java 魔法支持！
2.  **Maven 3+**：构建后端的得力助手。
3.  **Node.js & npm**：搞定前端的必备工具。
4.  **MySQL 9.x**：数据的大本营（版本 8+ 也可以试试哦）。

**部署步骤：**

1.  **克隆仓库**：把代码带回家！
    ```bash
    git clone <repository-url>
    cd disruptorDemo
    ```

2.  **初始化数据库**：
    找到根目录下的 `table.sql`，在你的 MySQL 数据库中执行它，建立我们的“根据地”。记得修改 `application.yml` 里的数据库配置哦！

3.  **启动后端（大本营）**：
    ```bash
    mvn spring-boot:run
    ```
    看到 Spring Boot 的 Logo 亮起，说明后勤保障已就绪！

4.  **启动前端（先锋队）**：
    ```bash
    cd demo
    npm install
    npm run dev
    ```
    打开浏览器访问控制台输出的地址（通常是 `http://localhost:5173`），战斗开始！

## 🎮 使用方法

1.  **登录入场**：打开页面，你就是一个等待协作的艺术家。（目前可能有模拟登录，直接进！）
2.  **开始表演**：点击“进入编辑”，你就拿到了指挥棒！
3.  **疯狂操作**：点击“放大”、“缩小”、“左旋”、“右旋”，看着控制台和界面上的反馈，那是指令在飞舞！
4.  **围观模式**：多开几个浏览器窗口，模拟多个用户。当你在一个窗口操作时，观察其他窗口——是不是瞬间同步了？这就叫“心有灵犀”！
5.  **把酒言欢**：在聊天框里发个消息，大家都能收到系统的实时广播。

## 🏗️ 项目结构树

来看看我们的基地布局：

```text
disruptorDemo/
├── demo/                   # 🎨 前端战场 (Vue 3 + Vite)
│   ├── src/
│   │   ├── components/     # 🧩 战斗组件 (Editor.vue - 核心战场)
│   │   ├── App.vue         # 📱 主入口
│   │   └── ...
│   ├── package.json        # 📦 前端军火库
│   └── ...
├── src/                    # ⚙️ 后端大本营 (Spring Boot)
│   ├── main/java/com/azer/disruptordemo/
│   │   ├── config/         # 🛠️ 战略部署 (WebSocket配置)
│   │   ├── controller/     # 🕹️ 指挥中心
│   │   ├── disruptor/      # 🚀 秘密武器 (Disruptor 核心实现)
│   │   │   ├── PictureEditEventProducer.java # 📨 消息投递员
│   │   │   └── PictureEditEventWorkHandler.java # 👷 硬核搬砖工
│   │   ├── websocket/      # 📡 通讯塔 (PictureEditHandler.java)
│   │   └── ...
│   └── ...
├── table.sql               # 🗺️ 战地地图 (数据库脚本)
└── pom.xml                 # 📜 补给清单 (Maven依赖)
```

## 🧠 核心方法解释

这里有几个关键的“魔法咒语”，了解它们你就能掌控雷电：

### 后端 (Java)

1.  **`PictureEditEventProducer.publishEvent(...)`**  
    *   **角色**：金牌快递员 📨
    *   **作用**：当 WebSocket 收到前端请求时，这个方法会把请求包装成一个“事件”，并极其快速地把它扔进 Disruptor 的环形缓冲区（RingBuffer）。它是连接外部请求和内部高速处理引擎的入口。

2.  **`PictureEditEventWorkHandler.onEvent(...)`**  
    *   **角色**：流水线操作工 👷
    *   **作用**：它是真正的干活人！从队列里取出事件，拆开包装，看看是要“进入编辑”还是“旋转图片”，然后调用相应的业务逻辑。因为是串行消费，所以这里不需要复杂的锁，快到飞起！

3.  **`PictureEditHandler.handleTextMessage(...)`**  
    *   **角色**：门卫大爷 🛡️
    *   **作用**：WebSocket 的第一道关卡。它收到前端发来的 JSON 字符串，不做复杂逻辑，直接转手交给 `Producer`，绝不拖泥带水，保证网络层的高响应。

### 前端 (Vue)

4.  **`connectWebSocket()`**  
    *   **角色**：通讯兵 📞
    *   **作用**：组件挂载时建立连接，监听 `onmessage`。一旦收到后端广播（比如有人旋转了图片），立刻更新 UI 或弹出提示，让用户感觉像是就在旁边操作一样。

## 🤝 贡献指南

觉得这个项目有点意思？想加点新功能？比如加个滤镜？或者做个多人聊天室？
热烈欢迎！🙌

1.  **Fork** 本仓库。
2.  新建你的 feature 分支 (`git checkout -b feature/AmazingFeature`)。
3.  提交你的修改 (`git commit -m 'Add some AmazingFeature'`)。
4.  推送到分支 (`git push origin feature/AmazingFeature`)。
5.  提交 **Pull Request**，我们一起探讨！

## 📄 许可证信息

本项目遵循 **MIT License** 开源协议。
这意味着你可以随意使用、修改、分发，只要你开心就好！但在使用时请保留原作者的版权声明哦。😉

---
*Generated with ❤️ by Gemini CLI*
