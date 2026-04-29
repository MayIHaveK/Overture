package priv.seventeen.artist.overture.core.meta

/**
 * Meta 键注解
 * 用于标记 Meta 实现类的注册键名
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MetaKey(val value: String)
