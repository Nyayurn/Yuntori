import {defineConfig} from 'vitepress'

export default defineConfig({
    title: "Yutori Next",
    description: "Yutori-Next's Documents",
    base: "/Yutori-Next/",
    themeConfig: {
        sidebar: [
            {text: "指南", link: "/Guild"},
            {text: "进阶", link: "/Advanced"},
            {text: "动作", link: "/Action"},
            {text: "事件", link: "/Event"}
        ]
    }
})