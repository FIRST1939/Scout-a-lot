# KMP 文件结构

本次按根目录 `architecture.md` 的目录规划整理 KMP 源文件，仅迁移文件并调整 package/import，保留业务逻辑。继续使用 Gradle 标准的 `shared/src/<sourceSet>/kotlin/` 路径。

```text
shared/src/commonMain/kotlin/com/frcteam1939/scouting2026/
├── AppContainer.kt                 # 保留现有 DataDependencyContainer 对象名称
├── ViewModelFactory.kt             # 应用级 ViewModel 装配
├── ShowAlert.kt
├── core/
│   ├── database/
│   │   ├── Database.kt
│   │   ├── dao/
│   │   ├── entity/
│   │   └── converter/
│   ├── network/
│   │   ├── TBA.kt
│   │   ├── FRCEvents.kt
│   │   └── dto/
│   └── storage/
└── feature/
    ├── settings/                   # SettingsRepository、SettingsViewModel
    ├── events/                     # 两个赛事 Repository、CommonRepoInteractions
    ├── scouting/                   # ScoutingViewModel、ScoutedDataRepository、
    │                              # FormFieldManager、PitScoutingAssignments、DemoEvent
    ├── forms/                      # FormsRepository、EditFormsViewModel、model/DataFields
    ├── reports/                    # DataViewModel、ChecklistViewModel
    └── sync/                       # FileSyncViewModel
```

`androidMain` 和 `iosMain` 中的 Database、网络认证、FileManager、ViewModelFactory 与 commonMain 使用对应目录和包名。Room schema 目录随 AppDatabase 全限定名迁移，历史 JSON 内容、数据库版本和表结构不变。

Android Compose UI、导航和蓝牙仍位于 `androidApp`；SwiftUI 仍位于 `iosApp`。Gradle 模块与依赖保持原样，未引入 Compose Multiplatform。

## 保留的现有结构问题

- SettingsViewModel 同时负责设置和赛事管理，并直接使用 TBA 客户端；本次未拆出 add/edit event ViewModel。
- 功能之间仍有 Repository、模型和辅助函数依赖；仅移动文件不会消除这些依赖。
- Entity/DTO 仍按原方式暴露给调用方，Repository 的读写接口未调整。
- Field、RankingData 等与 ViewModel 同文件的声明保持原位；未新增当前不存在的 AppTheme 或共享传输协议模型。
- ViewModelFactory 属于应用级装配，和依赖容器一起放在根包，避免 core 依赖各 feature。

这些问题需要后续单独决定是否重构，本次不实施文档中的数据库迁移或 CMP 路线图。
