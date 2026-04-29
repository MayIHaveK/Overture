# 事件模型

事件模型允许将事件脚本定义为可复用的模板，多个物品可以引用同一个模型。

## 定义模型

在物品配置文件中，以 `$` 结尾的键名定义事件模型：

```yaml
# items/weapons.yml

# 事件模型定义（$ 后缀）
base_weapon$:
  event:
    on_drop!!: |
      cancel()
      sendMessage('&c武器不可丢弃！')
    on_pick: |
      sendMessage('&a你捡起了一把武器')

# 物品引用模型
diamond_sword:
  display: weapon_display
  icon: DIAMOND_SWORD
  event:
    from: [base_weapon]          # 引用模型（去掉 $ 后缀）
    on_right_click: |            # 物品自身的事件
      sendMessage('&a右键！')
```

## 引用规则

- `event.from` 接受模型 ID 列表（逗号分隔或 YAML 列表）
- 多个模型的事件通过合并处理，后加载的模型覆盖同名事件
- 物品自身定义的事件优先级最高，会覆盖模型中的同名事件

## 动作解析优先级

1. 物品自身的 `event.on_xxx`
2. 引用模型的 `event.on_xxx`（按 `from` 列表顺序，后面的覆盖前面的）
