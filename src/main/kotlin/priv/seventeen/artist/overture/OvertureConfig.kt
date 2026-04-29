package priv.seventeen.artist.overture

import priv.seventeen.artist.blink.bukkitPlugin
import priv.seventeen.artist.blink.config.BlinkConfig
import priv.seventeen.artist.blink.config.BlinkSection
import priv.seventeen.artist.blink.config.Comment
import priv.seventeen.artist.blink.config.ConfigKey

/**
 * Overture 主配置
 */
class OvertureConfig : BlinkConfig(bukkitPlugin, "config") {

    var update: UpdateSection = UpdateSection()
    var durability: DurabilitySection = DurabilitySection()
    var cooldown: CooldownSection = CooldownSection()

    class UpdateSection : BlinkSection() {
        @Comment("玩家加入时检查背包物品更新")
        @ConfigKey("check-on-join")
        var checkOnJoin: Boolean = true

        @Comment("拾取物品时检查更新")
        @ConfigKey("check-on-pickup")
        var checkOnPickup: Boolean = true

        @Comment("切换手持物品时检查更新")
        @ConfigKey("check-on-switch")
        var checkOnSwitch: Boolean = true

        @Comment("定时检查周期 (tick, 20 tick = 1 秒)")
        @ConfigKey("async-tick-period")
        var asyncTickPeriod: Long = 100L
    }

    class DurabilitySection : BlinkSection() {
        @Comment("耐久条显示格式, 可用变量: %symbol% %current% %max% %percent%")
        var display: String = "&8[ &f%symbol% &8]"

        @ConfigKey("display-symbol")
        var displaySymbol: DurabilitySymbolSection = DurabilitySymbolSection()

        @Comment("耐久条最大格数")
        var scale: Int = 20
    }

    class DurabilitySymbolSection : BlinkSection() {
        var full: String = "◆"
        var empty: String = "◇"
    }

    class CooldownSection : BlinkSection() {
        @Comment("冷却中提示消息")
        var message: String = "&c冷却中，剩余 %time% 秒"
    }

    companion object {
        lateinit var instance: OvertureConfig
            private set

        fun load() {
            instance = OvertureConfig()
            instance.load()
        }

        fun reload() {
            instance.reload()
        }
    }
}
