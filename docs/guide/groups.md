# 分组系统

物品分组通过 `items/` 目录下的**文件夹结构**自动生成。每个文件夹对应一个分组，支持任意层级嵌套。

## 基础用法

```
plugins/Overture/items/
├── weapons/              # 分组 "weapons"
│   ├── sword.yml
│   └── bow.yml
├── armor/                # 分组 "armor"
│   ├── leather/          # 子分组 "armor/leather"
│   │   └── chestplate.yml
│   └── iron/             # 子分组 "armor/iron"
│       └── helmet.yml
└── misc.yml              # 无分组
```

扫描规则：
- 以 `__` 开头的文件和键名被跳过
- `__group__.yml` 是分组配置文件（见下文），不作为物品加载
- 以 `$` 结尾的键注册为事件模型，不作为物品

## 分组配置

在任意分组目录下放置 `__group__.yml` 可以自定义分组的显示效果。所有字段都是可选的。

```yaml
# plugins/Overture/items/weapons/__group__.yml

priority: 10              # 排序优先级（越小越靠前，默认 0）
icon: DIAMOND_SWORD       # 分组图标（Bukkit Material，默认 CHEST）
name: "&b武器"             # GUI 显示名（支持颜色码，默认为文件夹名）
lore:                     # 分组描述
  - "&7各类武器收藏"
  - "&7包含剑、弓、斧等"
```

## 在 GUI 中浏览

使用 `/ot list` 打开物品菜单。菜单布局：

- **第 0 行**：面包屑导航（罗盘 = 根目录，每级分组一个图标，星星标记当前位置），点击可跳转
- **第 1-4 行**：内容区，先展示子分组（按 `priority` 排序），后展示物品
- **第 5 行**：功能栏
  - 左箭头（槽位 45）：上一页
  - 门/屏障（槽位 49）：返回上级分组 / 关闭菜单
  - 右箭头（槽位 53）：下一页

也可以直接打开某个分组：

```
/ot list weapons
/ot list armor/iron
```

分组路径用 `/` 分隔，与文件夹结构一一对应。

## 命令

| 命令 | 说明 |
|------|------|
| `/ot list` | 打开根级别菜单 |
| `/ot list <group>` | 直接打开指定分组（支持 tab 补全） |

## 完整示例

```
items/
├── __group__.yml                    # 根分组配置
├── weapons/
│   ├── __group__.yml                # priority: 10, icon: DIAMOND_SWORD
│   ├── swords.yml
│   └── rare/
│       ├── __group__.yml            # priority: 1, icon: NETHERITE_SWORD
│       └── legendary_sword.yml
├── armor/
│   ├── __group__.yml                # priority: 20, icon: DIAMOND_CHESTPLATE
│   └── sets.yml
└── consumables.yml                  # 无分组，直接出现在根菜单
```

菜单顺序：`weapons` → `armor` →（根级物品）`consumables.yml` 里的所有物品。

进入 `weapons` 后顺序：`rare` 子分组 →（`weapons` 根级物品）`swords.yml` 里的所有物品。
