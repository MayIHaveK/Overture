---
layout: home

hero:
  name: Overture
  text: 下一代 Minecraft 物品库插件
  tagline: 基于 Blink 框架 · Asteroid NMS · Aria 脚本引擎
  image:
    src: /logo.svg
    alt: Overture
  actions:
    - theme: brand
      text: 快速开始
      link: /guide/getting-started
    - theme: alt
      text: API 参考
      link: /dev/api
    - theme: alt
      text: GitHub
      link: https://github.com/17Artist/Overture

features:
  - icon: 🔄
    title: 物品流设计
    details: 所有物品操作通过 ItemStream 中间层，避免直接操作 ItemStack
  - icon: 🔒
    title: 版本签名自动更新
    details: 配置变更自动检测 SHA-1 签名，玩家背包物品实时同步
  - icon: 📌
    title: '!! 锁定机制'
    details: 精细控制更新时哪些数据保留、哪些强制覆盖
  - icon: 🎨
    title: 展示方案分离
    details: Display 独立定义，多物品共享同一展示模板，支持条件展示
  - icon: 📜
    title: Aria 脚本驱动
    details: 15 种事件触发点，22 个内置函数，预编译高性能执行
  - icon: 🧩
    title: 12 种内置 Meta
    details: 属性、附魔、耐久、唯一、模型数据、发光等开箱即用
  - icon: 🗺️
    title: 数据映射器
    details: 8 种内置函数将 NBT 数据自动转换为展示变量
  - icon: 🔌
    title: 多源物品提供
    details: ItemProvider 接口支持 YAML、数据库等任意来源扩展
---
