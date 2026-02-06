# CSS to Kobweb - iFlow 上下文文件

## 项目概述

**css2kobweb** 是一个将 CSS 代码转换为 Kobweb 修饰符（modifiers）和样式的工具。该项目基于 Kobweb 框架构建，提供了一个在线转换器，允许开发者将传统 CSS 代码迁移到 Kobweb 的 Compose 风格声明式 UI 框架中。

### 主要技术栈

- **语言**: Kotlin (多平台项目)
- **前端框架**: Kobweb (基于 Compose Multiplatform)
- **构建工具**: Gradle (Kotlin DSL)
- **目标平台**: 
  - JVM (用于开发和测试)
  - JavaScript/浏览器 (用于 Web 应用)

### 项目结构

```
css2kobweb/
├── parsing/          # CSS 解析核心模块 (多平台共享代码)
│   └── src/
│       ├── commonMain/kotlin/  # 核心解析逻辑
│       │   ├── constants/      # CSS 规则、颜色、单位等常量
│       │   ├── functions/      # 渐变、颜色、位置等函数处理
│       │   └── *.kt           # 主要解析和转换类
│       └── jvmMain/kotlin/     # JVM 入口点和数据生成
└── site/             # Kobweb Web 应用模块
    └── src/jsMain/kotlin/      # 前端页面和组件
```

### 核心功能

- **CSS 解析**: 解析 CSS 选择器和属性声明
- **样式转换**: 将 CSS 属性转换为 Kobweb 修饰符
- **变量内联**: 支持 CSS 变量（`var(--name)`）的内联替换
- **伪类/伪元素处理**: 支持伪类（`:hover`, `:focus` 等）和伪元素（`::before`, `::after`）
- **媒体查询**: 支持 `@media` 查询
- **关键帧动画**: 支持 `@keyframes` 动画
- **智能提取**: 自动提取公共修饰符以减少代码重复

---

## 构建和运行

### 环境要求

- **Java**: JDK 17 或更高版本
- **Gradle**: 项目包含 Gradle Wrapper (版本通过 `gradle/wrapper/gradle-wrapper.properties` 定义)
- **Node.js**: 用于 Kobweb 的浏览器依赖

### 本地开发

1. **启动开发服务器**:
   ```bash
   cd site
   kobweb run
   ```
   访问 http://localhost:8080 查看应用

2. **在解析模块运行 JVM 入口**:
   ```bash
   ./gradlew :parsing:run
   ```

3. **运行解析模块测试**:
   ```bash
   ./gradlew :parsing:test
   ```

### 构建和导出

1. **导出静态站点**:
   ```bash
   cd site
   kobweb export --layout static
   ```

2. **生产环境运行**:
   ```bash
   kobweb run --env prod --notty
   ```

### Gradle 命令

- **构建所有模块**: `./gradlew build`
- **清理构建**: `./gradlew clean`
- **构建解析模块**: `./gradlew :parsing:build`
- **构建站点模块**: `./gradlew :site:build`
- **生成浏览器缓存 ID**: `./gradlew :site:kobwebBrowserCacheId`

---

## 开发约定

### 代码风格

- **语言**: Kotlin
- **命名约定**:
  - 函数和变量: camelCase
  - 类和接口: PascalCase
  - 常量: UPPER_SNAKE_CASE (主要用于 CSS 属性和单位映射)
- **文件组织**: 按功能模块分组（constants/, functions/）

### 项目依赖版本

当前版本定义在 `gradle/libs.versions.toml`:
- Kotlin: 2.2.0
- JetBrains Compose: 1.8.0
- Kobweb: 0.23.0

### 解析模块架构

**核心类**:
- `Css2Kobweb.kt`: 主入口函数 `css2kobweb()`，处理完整的 CSS 转换流程
- `CSSParser.kt`: CSS 解析器，提取选择器和属性
- `Properties.kt`: 属性解析和映射逻辑
- `StyleModifier.kt`: 样式修饰符的表示（Inline/Local/Global）
- `PostProcessing.kt`: 后处理，包括变量内联
- `ColoredCode.kt`: 代码高亮输出

**常量模块** (`constants/`):
- `Colors.kt`: 颜色名称和值映射
- `CssRules.kt`: CSS 伪类/伪元素规则映射
- `ShorthandProperties.kt`: CSS 简写属性展开
- `Units.kt`: CSS 单位定义

**函数模块** (`functions/`):
- `Color.kt`: 颜色值解析 (RGB, HSL, HEX 等)
- `Gradient.kt`: 渐变相关函数
- `Position.kt`: 位置相关解析
- `Transition.kt`: 过渡和动画

### 站点模块架构

**页面**: `pages/Index.kt` - 主页面，包含 CSS 输入和 Kotlin 代码输出

**组件**:
- `layouts/PageLayout.kt`: 页面布局
- `sections/Footer.kt`: 页脚
- `styles/Background.kt`: 背景样式
- `widgets/KotlinCode.kt`: Kotlin 代码高亮显示组件

**样式系统**: 使用 Kobweb 的 `CssStyle` DSL 定义可复用样式

### Git 工作流

- **主分支**: `master`
- **部署**: 推送到 `master` 分支会触发 GitHub Actions 工作流，自动构建并部署到 GitHub Pages
- **提交信息**: 遵循简洁明了的约定，描述变更的"原因"而非仅描述"内容"

### 测试策略

- 解析逻辑应通过 `Main.kt` 中的测试用例进行验证
- 在进行 CSS 解析变更时，确保现有的 `rawCSS`, `rawCSS2`, `rawCss3` 等测试用例仍然正确转换

---

## 关键文件说明

### 配置文件

- `build.gradle.kts`: 根项目配置
- `parsing/build.gradle.kts`: 解析模块多平台配置
- `site/build.gradle.kts`: Kobweb 应用配置
- `gradle/libs.versions.toml`: 依赖版本管理
- `site/.kobweb/conf.yaml`: Kobweb 应用配置

### 核心源文件

- `parsing/src/commonMain/kotlin/io/github/opletter/css2kobweb/Css2Kobweb.kt`: 主要转换逻辑
- `parsing/src/jvmMain/kotlin/io/github/opletter/css2kobweb/Main.kt`: JVM 测试入口
- `site/src/jsMain/kotlin/io/github/opletter/css2kobweb/pages/Index.kt`: Web 应用主页面

### 资源文件

- `parsing/src/jvmMain/resources/colors.txt`: CSS 颜色名称列表
- `parsing/src/jvmMain/resources/units.txt`: CSS 单位列表

---

## CI/CD

项目使用 GitHub Actions 进行持续集成和部署：

**工作流**: `.github/workflows/build_and_deploy_site.yml`

**流程**:
1. 推送到 `master` 分支时触发
2. 设置 Java 17 环境
3. 设置 Gradle
4. 缓存 Playwright 浏览器依赖
5. 下载 Kobweb CLI
6. 导出静态站点 (`kobweb export --layout static`)
7. 部署到 GitHub Pages

---

## 特殊注意事项

### CSS 变量处理

- CSS 变量（`var(--name)`）会在解析前被内联替换
- 变量查找和替换按反向顺序进行，确保嵌套变量正确处理

### 选择器解析

- 支持多类选择器（如 `.class1.class2`）
- 支持组合选择器（如 `.button, .test`）
- 支持伪类和伪元素
- 支持嵌套选择器

### 代码生成策略

- 当多个选择器共享相同样式时，会自动提取公共修饰符
- 提取方式根据选择器数量和基础名称决定（Inline/Local/Global）
- 生成的 Kotlin 代码包含语法高亮

### 性能优化

- 输入超过 5000 字符时自动添加 50ms 防抖延迟
- 使用 `LaunchedEffect` 避免在每次输入时立即触发转换

---

## 开发建议

1. **添加新的 CSS 属性支持**: 在 `constants/` 中添加属性映射，在 `Properties.kt` 中添加解析逻辑
2. **添加新的 CSS 函数**: 在 `functions/` 中创建对应的解析函数
3. **调试解析问题**: 使用 `Main.kt` 中的测试用例进行迭代调试
4. **测试新功能**: 添加到 `Main.kt` 的测试 CSS 字符串中
5. **前端调试**: 使用浏览器开发者工具查看转换后的结果

---

## 已知限制

- 不支持 `@import`、`@charset`、`@namespace` 规则（会被自动过滤）
- 注释会被自动移除
- 单引号会被替换为双引号以简化解析
- 某些高级 CSS 特性可能尚未完全支持

---

## 许可证

本项目采用开源许可证，具体信息请参阅 `LICENSE` 文件。

---

**生成日期**: 2026-02-06
**项目 URL**: https://github.com/opletter/css2kobweb