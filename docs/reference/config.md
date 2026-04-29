# 主配置参考

`config.yml` 完整配置项。

```yaml
# 自动更新配置
update:
  # 玩家加入时检查背包物品更新
  check-on-join: true
  # 拾取物品时检查更新
  check-on-pickup: true
  # 切换手持物品时检查更新
  check-on-switch: true
  # 定时检查周期 (tick, 20 tick = 1 秒)
  async-tick-period: 100

# 耐久条全局配置
durability:
  # 耐久条显示格式
  # 可用变量: %symbol% %current% %max% %percent%
  display: "&8[ &f%symbol% &8]"
  # 耐久条符号
  display-symbol:
    full: "◆"
    empty: "◇"
  # 耐久条最大格数
  scale: 20

# 物品冷却
cooldown:
  # 冷却中提示消息
  message: "&c冷却中，剩余 %time% 秒"
```

## 更新时机

| 时机   | 配置项                 | 说明          |
|------|---------------------|-------------|
| 玩家加入 | `check-on-join`     | 延迟 1 秒后遍历背包 |
| 拾取物品 | `check-on-pickup`   | 单物品检查       |
| 切换手持 | `check-on-switch`   | 单物品检查       |
| 切换世界 | —                   | 始终启用        |
| 定时检查 | `async-tick-period` | 遍历所有在线玩家背包  |
