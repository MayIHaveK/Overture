---
layout: home

hero:
  name: Overture
  text: 下一代 Minecraft 物品库
  tagline: 为现代服务端设计的物品系统 · 版本感知 · 脚本驱动 · 动态展示
  image:
    src: /logo.svg
    alt: Overture
  actions:
    - theme: brand
      text: 快速开始 →
      link: /guide/getting-started
    - theme: alt
      text: 查看 API
      link: /dev/api
    - theme: alt
      text: GitHub
      link: https://github.com/17Artist/Overture

features:
  - icon:
      src: /icons/stream.svg
      width: 36
      height: 36
    title: 物品流 (ItemStream)
    details: 统一的物品操作中间层，不再直接操作 ItemStack。NBT 读写、事件链、构建释放全部流水线化。
  - icon:
      src: /icons/shield-check.svg
      width: 36
      height: 36
    title: 版本签名自动同步
    details: 配置变更自动计算 SHA-1 指纹。玩家背包中的物品检测到版本过期会无缝更新到最新定义。
  - icon:
      src: /icons/lock.svg
      width: 36
      height: 36
    title: '!! 锁定控制'
    details: 精确控制更新策略。字段级锁定决定哪些数据保留玩家状态、哪些强制同步最新配置。
  - icon:
      src: /icons/layers.svg
      width: 36
      height: 36
    title: 展示方案分离
    details: Display 独立配置，多个物品共享同一模板。支持基于 Aria 表达式的条件展示切换。
  - icon:
      src: /icons/zap.svg
      width: 36
      height: 36
    title: Aria 脚本引擎
    details: 15 种事件触发点，22 个内置函数，脚本预编译。物品行为由脚本驱动，热重载即时生效。
  - icon:
      src: /icons/blocks.svg
      width: 36
      height: 36
    title: 12 种内置 Meta
    details: 属性、附魔、自定义耐久、唯一标记、模型数据、发光、药水、头颅等开箱即用，支持自定义扩展。
---
