import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'Overture',
  description: '下一代 Minecraft 物品库插件',
  lang: 'zh-CN',
  base: '/Overture/',

  head: [
    ['link', { rel: 'icon', href: '/Overture/logo.svg' }]
  ],

  themeConfig: {
    logo: '/logo.svg',

    nav: [
      { text: '指南', link: '/guide/getting-started', activeMatch: '/guide/' },
      { text: '配置参考', link: '/reference/item-config', activeMatch: '/reference/' },
      { text: '开发者', link: '/dev/api', activeMatch: '/dev/' }
    ],

    sidebar: {
      '/guide/': [
        {
          text: '使用指南',
          items: [
            { text: '概述', link: '/guide/overview' },
            { text: '快速开始', link: '/guide/getting-started' },
            { text: '物品配置', link: '/guide/item-config' },
            { text: '分组系统', link: '/guide/groups' },
            { text: '展示方案', link: '/guide/display' },
            { text: '动作系统', link: '/guide/action' },
            { text: '数据映射', link: '/guide/data-mapper' },
            { text: '事件模型', link: '/guide/model' },
            { text: '命令', link: '/guide/commands' }
          ]
        }
      ],
      '/reference/': [
        {
          text: '配置参考',
          items: [
            { text: '物品配置', link: '/reference/item-config' },
            { text: '展示方案', link: '/reference/display' },
            { text: 'Meta 系统', link: '/reference/meta' },
            { text: 'Aria 内置函数', link: '/reference/aria-functions' },
            { text: '映射函数', link: '/reference/mapper-functions' },
            { text: '主配置', link: '/reference/config' }
          ]
        }
      ],
      '/dev/': [
        {
          text: '开发者',
          items: [
            { text: 'API 参考', link: '/dev/api' },
            { text: '事件系统', link: '/dev/events' },
            { text: '架构设计', link: '/dev/architecture' }
          ]
        }
      ]
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/17Artist/Overture' }
    ],

    footer: {
      message: 'Released under the Apache 2.0 License.',
      copyright: 'Copyright © 2026 17Artist'
    },

    search: {
      provider: 'local'
    },

    outline: {
      level: [2, 3],
      label: '目录'
    },

    docFooter: {
      prev: '上一页',
      next: '下一页'
    }
  }
})
