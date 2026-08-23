# 用户角色分配（阶段 4 任务 2）实施计划

> **执行模式**：本计划执行者是用户本人（学习模式）——实现代码由用户手写，计划锁定接口签名、SQL 行为、测试断言与验证命令；每个任务结束后由 AI review 把关。
> 步骤使用 checkbox（`- [ ]`）语法跟踪。测试统一用 `mvn test` 运行。

**Goal:** 实现「给用户分配角色」全量覆盖接口 `PUT /user/{userId}/roles` 与回显接口 `GET /user/{userId}/roles`。

**Architecture:** User 模块新增 `UserRoleDao` 直接操作 `sys_user_role` 关联表（表已建好，联合唯一键 `uk_user_role(user_id, role_id)`）；角色存在性校验跨模块走 `RoleService` 应用层批量方法；分配采用「先删后插」全量覆盖，`@Transactional` 放在应用层方法上。

**Tech Stack:** Spring Boot 4.0.7 / Java 21 / MyBatis 4.0.1 / JUnit Jupiter 6.0.3 / Mockito / Lombok

**Spec:** 无独立 spec 文件（bounded 任务，设计在对话中确认通过）。设计定稿摘要见下节。

## Global Constraints

- 测试**只能用命令行 `mvn test` 运行**：IDEA 2024.1.4 内置运行器与 JUnit 6 不兼容（报 `NoSuchMethodError`），IDE 里跑会失败
- `@Transactional` 只能放应用层方法（六边形架构规则）；本项目此前从未使用过，本次为首次引入
- 跨模块调用必须走应用层 facade：UserServiceImpl 校验角色存在性必须调 `RoleService` 方法，禁止注入 `RoleDao`
- 复用错误码，**不新增 ResponseCode**：`10004 用户不存在`、`10104 角色不存在`
- 分配语义：**全量覆盖**——前端提交完整 roleIds，后端先删后插；空列表合法 = 清空全部角色
- 实体字段 Javadoc 与数据库列 COMMENT 对齐；DTO 校验用 jakarta.validation + 中文 message
- `BusinessException` 继承 `RuntimeException` → `@Transactional` 默认回滚行为可用，无需 `rollbackFor`
- 提交信息风格沿用现有惯例：`Period4 #comment <说明>`

## 设计定稿摘要（含一处勘误）

- **勘误**：此前设计曾建议"deleteUser 连带清理关联表"。经核实 `deleteUser` 是**逻辑删除**（`status=2`，行保留），且用户删除后绑定保留可为将来"恢复用户"留余地，因此**本计划不修改 `deleteUser`**，用户逻辑删除后 `sys_user_role` 绑定保留
- **DTO 偏差说明**：TASKLIST 原规划 `AssignRoleRequestBody(userId+roleIds)`，实际 userId 走路径参数，body 只含 roleIds，避免双源不一致
- 校验链（assignRoles）：① userId 存在且非删除状态 → 10004 ② roleIds 去重 ③ 非空则批量校验角色存在性 → 10104 ④ `@Transactional` 先删后插
- 回显返回 `List<String>`（roleIds），与菜单回显"返回已绑 id"规划一致

## 文件结构

| 动作 | 文件 | 职责 |
|---|---|---|
| Create | `src/main/java/com/jiege/community/entity/UserRole.java` | 关联实体，对应 sys_user_role |
| Create | `src/main/java/com/jiege/community/dao/UserRoleDao.java` | 关联表 DAO 接口 |
| Create | `src/main/resources/mapper/UserRoleDao.xml` | 关联表 SQL（mapper-locations 通配 `classpath:mapper/*.xml`，无需改配置） |
| Create | `src/main/java/com/jiege/community/dto/AssignRoleRequestBody.java` | 分配请求体（仅 roleIds） |
| Modify | `src/main/java/com/jiege/community/dao/RoleDao.java` | 加批量存在性查询方法 |
| Modify | `src/main/resources/mapper/RoleDao.xml` | 对应 SQL |
| Modify | `src/main/java/com/jiege/community/service/RoleService.java` + `impl/RoleServiceImpl.java` | 加应用层批量校验方法 |
| Modify | `src/main/java/com/jiege/community/service/UserService.java` + `impl/UserServiceImpl.java` | 加 assignRoles / getRoleIdsByUserId |
| Modify | `src/main/java/com/jiege/community/controller/UserController.java` | 加两个端点 |
| Modify | `docs/TASKLIST.md` | 勾选阶段 4 任务 2 |

---

### Task 0: 预备——提交工作区残留改动

**Files:**
- Commit（无代码改动）: `src/main/java/com/jiege/community/common/utils/UserContext.java`、`src/main/java/com/jiege/community/service/impl/MenuServiceImpl.java`、`src/main/java/com/jiege/community/vo/MenuVO.java`

**Interfaces:**
- Produces: 干净的工作区，供任务 1~5 独立提交

- [ ] **Step 1: 自查残留改动**

Run: `git diff --stat`
Expected: 恰好 3 个文件（UserContext.java / MenuServiceImpl.java / MenuVO.java），无其他意外改动

- [ ] **Step 2: 提交**

```bash
git add src/main/java/com/jiege/community/common/utils/UserContext.java src/main/java/com/jiege/community/service/impl/MenuServiceImpl.java src/main/java/com/jiege/community/vo/MenuVO.java
git commit -m "Period4 #comment 代码整洁：MenuVO 字段 final 化、MenuServiceImpl 抽取 buildMenu、UserContext 补注释"
```

- [ ] **Step 3: 验证工作区干净**

Run: `git status --short`
Expected: 无输出

---

### Task 1: 数据层——UserRole 实体 + UserRoleDao + XML

**Files:**
- Create: `src/main/java/com/jiege/community/entity/UserRole.java`
- Create: `src/main/java/com/jiege/community/dao/UserRoleDao.java`
- Create: `src/main/resources/mapper/UserRoleDao.xml`
- Test: `src/test/java/com/jiege/community/dao/UserRoleDaoTest.java`

**Interfaces:**
- Produces（后续任务依赖的精确签名）:
  - `UserRole`：Lombok `@Data @Builder @NoArgsConstructor @AllArgsConstructor`，字段 `Long id` / `String userId` / `String roleId`
  - `UserRoleDao.batchInsert(@Param("list") List<UserRole> list)` → `int`
  - `UserRoleDao.deleteByUserId(@Param("userId") String userId)` → `int`
  - `UserRoleDao.selectRoleIdsByUserId(@Param("userId") String userId)` → `List<String>`

- [ ] **Step 1: 写失败测试** `UserRoleDaoTest`

测试类标 `@SpringBootTest`，每个测试方法标 `@Transactional`（Spring 测试事务，方法结束自动回滚，不污染 auth 库；要求本地 MySQL 已启动）。测试数据用固定随机 UUID 串直插关联表（表间无外键，不需要真实用户/角色存在）。

用例：
1. `batchInsert_thenSelect_returnsAllRoleIds`：构造 userId="test-user-xxxx" + 两个 roleId 的 `List<UserRole>` → `batchInsert` → `selectRoleIdsByUserId` → 断言返回列表 size==2 且包含这两个 roleId
2. `deleteByUserId_thenSelect_returnsEmpty`：先 `batchInsert` 一条 → `deleteByUserId` → 再 `selectRoleIdsByUserId` → 断言返回空列表

- [ ] **Step 2: 运行测试验证失败**

Run: `mvn test -Dtest=UserRoleDaoTest`
Expected: FAIL（`UserRoleDaoTest` 引用的类不存在，编译失败）

- [ ] **Step 3: 写最小实现**

1. `entity/UserRole.java`：Lombok 四注解；三个字段的 Javadoc 与表 COMMENT 对齐（`id` 数据库主键（自增）/ `userId` 用户业务ID / `roleId` 角色业务ID）
2. `dao/UserRoleDao.java`：`@Mapper` 接口，三个方法签名见 Interfaces，参数一律 `@Param`
3. `mapper/UserRoleDao.xml`：namespace `com.jiege.community.dao.UserRoleDao`，三条 SQL：
   - `batchInsert`：`INSERT INTO sys_user_role (user_id, role_id) VALUES` + `<foreach collection="list" item="item" separator=",">(#{item.userId}, #{item.roleId})</foreach>`
   - `deleteByUserId`：`DELETE FROM sys_user_role WHERE user_id = #{userId}`
   - `selectRoleIdsByUserId`：`SELECT role_id FROM sys_user_role WHERE user_id = #{userId} ORDER BY id`，`resultType="java.lang.String"`（`ORDER BY id` 保证回显顺序稳定）

- [ ] **Step 4: 运行测试验证通过**

Run: `mvn test -Dtest=UserRoleDaoTest`
Expected: PASS（2/2）

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/jiege/community/entity/UserRole.java src/main/java/com/jiege/community/dao/UserRoleDao.java src/main/resources/mapper/UserRoleDao.xml src/test/java/com/jiege/community/dao/UserRoleDaoTest.java
git commit -m "Period4 #comment 用户角色关联数据层：UserRole 实体 + UserRoleDao + XML"
```

---

### Task 2: RoleService 批量存在性校验方法

**Files:**
- Modify: `src/main/java/com/jiege/community/dao/RoleDao.java`（加方法声明）
- Modify: `src/main/resources/mapper/RoleDao.xml`（加 SQL）
- Modify: `src/main/java/com/jiege/community/service/RoleService.java`（加方法声明）
- Modify: `src/main/java/com/jiege/community/service/impl/RoleServiceImpl.java`（加实现）
- Test: `src/test/java/com/jiege/community/service/impl/RoleServiceImplTest.java`

**Interfaces:**
- Produces:
  - `RoleService.selectExistingRoleIds(List<String> roleIds)` → `List<String>`：返回传入列表中真实存在的 role_id 子集（供任务 3 做差集校验）
  - `RoleDao.selectExistingRoleIds(@Param("roleIds") List<String> roleIds)` → `List<String>`

- [ ] **Step 1: 写失败测试** `RoleServiceImplTest`

Mockito 单测（`@ExtendWith(MockitoExtension.class)` + `@Mock RoleDao` + `@InjectMocks RoleServiceImpl`），不连库。
用例：`selectExistingRoleIds_delegatesToDao`——mock `roleDao.selectExistingRoleIds(["r1","r2"])` 返回 `["r1"]`，调用 service 方法断言返回 `["r1"]`，并 `verify` dao 方法调用 1 次。

- [ ] **Step 2: 运行测试验证失败**

Run: `mvn test -Dtest=RoleServiceImplTest`
Expected: FAIL（`selectExistingRoleIds` 方法不存在，编译失败）

- [ ] **Step 3: 写最小实现**

1. `RoleDao.java` 加方法声明（签名见 Interfaces）
2. `RoleDao.xml` 加 SQL：`SELECT role_id FROM sys_role WHERE role_id IN <foreach collection="roleIds" item="rid" open="(" separator="," close=")">#{rid}</foreach>`，`resultType="java.lang.String"`
3. `RoleService.java` 加声明；`RoleServiceImpl.java` 实现为直接委托 `roleDao.selectExistingRoleIds`
   - 注意：此方法**不处理空列表**——`IN ()` 是非法 SQL，防御责任在调用方（任务 3 校验链保证非空才调用），方法 Javadoc 里注明该前置条件

- [ ] **Step 4: 运行测试验证通过**

Run: `mvn test -Dtest=RoleServiceImplTest`
Expected: PASS（1/1）

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/jiege/community/dao/RoleDao.java src/main/resources/mapper/RoleDao.xml src/main/java/com/jiege/community/service/RoleService.java src/main/java/com/jiege/community/service/impl/RoleServiceImpl.java src/test/java/com/jiege/community/service/impl/RoleServiceImplTest.java
git commit -m "Period4 #comment RoleService 增加角色批量存在性校验方法"
```

---

### Task 3: UserService 分配与回显（核心任务）

**Files:**
- Modify: `src/main/java/com/jiege/community/service/UserService.java`
- Modify: `src/main/java/com/jiege/community/service/impl/UserServiceImpl.java`
- Test: `src/test/java/com/jiege/community/service/impl/UserServiceImplTest.java`

**Interfaces:**
- Consumes: `UserRoleDao` 三方法（Task 1）、`RoleService.selectExistingRoleIds`（Task 2）、`UserDao.selectByUserId`
- Produces:
  - `UserService.assignRoles(String userId, List<String> roleIds)` → `void`
  - `UserService.getRoleIdsByUserId(String userId)` → `List<String>`
- 构造器变化：`UserServiceImpl(UserDao, PasswordEncoder, UserRoleDao, RoleService)`（新增后两个依赖）

- [ ] **Step 1: 写失败测试** `UserServiceImplTest`

Mockito 单测，mock 全部 4 个依赖（`UserDao`、`PasswordEncoder`、`UserRoleDao`、`RoleService`），`@InjectMocks UserServiceImpl`。User 用 `User.builder()` 构造。共 8 个用例：

1. `assignRoles_userNotExists_throws10004`：mock `userDao.selectByUserId` 返回 null → `assertThrows(BusinessException)` 且 `getCode()==10004`
2. `assignRoles_deletedUser_throws10004`：mock 返回 `status=UserStatus.DELETE.getStatus()` 的 User → 同上断言 10004
3. `assignRoles_roleNotExists_throws10104`：user 正常；传 `["r1","r2"]`，mock `roleService.selectExistingRoleIds` 只返回 `["r1"]` → 断言 10104
4. `assignRoles_emptyList_onlyDeletes`：传 `[]` → verify `deleteByUserId` 调用 1 次、`batchInsert` 从未调用、`selectExistingRoleIds` 从未调用（空列表不查库）
5. `assignRoles_duplicateIds_dedupes`：传 `["r1","r1","r2"]`，mock 校验返回全存在 → 用 `ArgumentCaptor<List<UserRole>>` 捕获 `batchInsert` 参数，断言 size==2
6. `assignRoles_success_deleteThenInsert`：`InOrder` 断言 `deleteByUserId` 先于 `batchInsert`；捕获的 UserRole 列表每个 `userId` 正确、roleId 与入参一致
7. `getRoleIdsByUserId_userNotExists_throws10004`：mock 返回 null → 10004
8. `getRoleIdsByUserId_returnsRoleIds`：mock `selectRoleIdsByUserId` 返回 `["r1","r2"]` → 断言返回相同列表

- [ ] **Step 2: 运行测试验证失败**

Run: `mvn test -Dtest=UserServiceImplTest`
Expected: FAIL（`assignRoles` / `getRoleIdsByUserId` 方法不存在，编译失败）

- [ ] **Step 3: 写最小实现**

`assignRoles` 校验链（顺序严格）：
1. `userDao.selectByUserId(userId)` 为 null → 抛 `USER_NOT_EXISTS(10004)`；`user.getStatus()` 等于 `UserStatus.DELETE.getStatus()` → 同样抛 10004（与 `getUserById` 对已删用户的处理保持一致）
2. roleIds 去重：`new LinkedHashSet<>(roleIds)` 转回 `List`（保持顺序）
3. 去重后列表非空：`existing = roleService.selectExistingRoleIds(去重列表)`；`existing.size() != 去重列表.size()` → 抛 `ROLE_NOT_EXISTS(10104)`
4. 方法标 `@Transactional`（`org.springframework.transaction.annotation.Transactional`，本项目首次引入）：
   - `userRoleDao.deleteByUserId(userId)`
   - 列表非空 → 组装 `List<UserRole>`（每个 roleId 一条，`UserRole.builder().userId(userId).roleId(roleId).build()`）→ `batchInsert`

`getRoleIdsByUserId`：同第 1 步校验（null 或 DELETE 状态 → 10004）→ 返回 `userRoleDao.selectRoleIdsByUserId(userId)`。

- [ ] **Step 4: 运行测试验证通过**

Run: `mvn test -Dtest=UserServiceImplTest`
Expected: PASS（8/8）

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/jiege/community/service/UserService.java src/main/java/com/jiege/community/service/impl/UserServiceImpl.java src/test/java/com/jiege/community/service/impl/UserServiceImplTest.java
git commit -m "Period4 #comment 用户分配角色与回显服务方法（全量覆盖 + 事务）"
```

---

### Task 4: DTO + Controller 端点

**Files:**
- Create: `src/main/java/com/jiege/community/dto/AssignRoleRequestBody.java`
- Modify: `src/main/java/com/jiege/community/controller/UserController.java`
- Test: `src/test/java/com/jiege/community/dto/AssignRoleRequestBodyTest.java`

**Interfaces:**
- Consumes: `UserService.assignRoles` / `getRoleIdsByUserId`（Task 3）
- Produces:
  - `PUT /user/{userId}/roles`（body: `AssignRoleRequestBody`）→ `ResponseEntity<HttpResponse<Void>>`
  - `GET /user/{userId}/roles` → `ResponseEntity<HttpResponse<List<String>>>`
- 无需改 `SecurityConfig`：新端点自动受 `anyRequest().authenticated()` 保护

- [ ] **Step 1: 写失败测试** `AssignRoleRequestBodyTest`

jakarta Validator 纯校验单测（`ValidatorFactory` 取 `Validator`），2 个用例：
1. `roleIds_isNull_violates`：`roleIds=null` → 断言存在 1 条校验违规且 message == `"角色列表不能为空"`
2. `roleIds_isEmpty_ok`：`roleIds=[]` → 断言无违规（空列表 = 清空语义，合法）

- [ ] **Step 2: 运行测试验证失败**

Run: `mvn test -Dtest=AssignRoleRequestBodyTest`
Expected: FAIL（DTO 类不存在，编译失败）

- [ ] **Step 3: 写最小实现**

1. `dto/AssignRoleRequestBody.java`：`@Data @NoArgsConstructor @AllArgsConstructor`；唯一字段 `@NotNull(message = "角色列表不能为空") private List<String> roleIds;`。**不要加 `@Size(min=1)`**——空列表是合法清空语义。Javadoc 说明用途
2. `UserController` 加两个方法（注释风格参照现有方法）：
   - `@PutMapping("/{userId}/roles")`：`assignRoles(@PathVariable String userId, @RequestBody @Valid AssignRoleRequestBody requestBody)` → 调 `userService.assignRoles(userId, requestBody.getRoleIds())` → `ResponseEntity.ok(HttpResponse.success())`
   - `@GetMapping("/{userId}/roles")`：`getRoleIds(@PathVariable String userId)` → `List<String> roleIds = userService.getRoleIdsByUserId(userId)` → `ResponseEntity.ok(HttpResponse.success(roleIds))`

- [ ] **Step 4: 运行测试验证通过**

Run: `mvn test -Dtest=AssignRoleRequestBodyTest`
Expected: PASS（2/2）

- [ ] **Step 5: 提交**

```bash
git add src/main/java/com/jiege/community/dto/AssignRoleRequestBody.java src/main/java/com/jiege/community/controller/UserController.java src/test/java/com/jiege/community/dto/AssignRoleRequestBodyTest.java
git commit -m "Period4 #comment 用户分配角色接口：PUT/GET /user/{userId}/roles"
```

---

### Task 5: 全链路联调 + 文档更新

**Files:**
- Modify: `docs/TASKLIST.md`

**Interfaces:**
- Consumes: 任务 1~4 的全部产出

- [ ] **Step 1: 启动应用**

Run: IDEA 启动 `UserAuthorApplication`（本地 MySQL `auth` 库已在运行）

- [ ] **Step 2: 准备测试数据**

1. `POST /jiege/auth/register` 注册新用户，记录返回的 `userId`
2. `POST /jiege/auth/login` 登录拿 token（后续请求带 `Authorization: Bearer <token>`）
3. `POST /jiege/role` 建两个测试角色，记录 `roleId`（记作 r1、r2）
   - 注意：PowerShell 的 `curl` 是 `Invoke-WebRequest` 别名，用 `curl.exe` 或 `Invoke-RestMethod` 发请求

- [ ] **Step 3: 执行联调清单**（每项记录实际结果）

| # | 用例 | 预期 |
|---|---|---|
| 1 | 无 token 调 `PUT /user/{userId}/roles` | 401 |
| 2 | 分配 `["r1","r2"]` → 调 `GET /user/{userId}/roles` | 200；回显含 r1、r2 各一次 |
| 3 | 覆盖分配 `["r2"]` → 回显 | 回显只剩 `["r2"]` |
| 4 | 分配 `[]` → 回显 | 200；回显 `[]`（清空生效） |
| 5 | 分配时 userId 传随机不存在值 | HTTP 200 + `code=10004` |
| 6 | 分配 roleIds 含随机不存在角色 | `code=10104` |
| 7 | 请求体 `{"roleIds": null}` | HTTP 400 + `code=40000` + message"角色列表不能为空" |
| 8 | 绑定 r1 后调 `DELETE /role/{r1}` | `code=10105`（阶段 2 校验仍生效） |
| 9 | 相同列表连续分配两次 | 两次均 200，回显不重复（联合唯一键未冲突） |
| 10 | 逻辑删除该用户后调 `GET /user/{userId}/roles` | `code=10004`（DELETE 状态被校验链拦截） |

- [ ] **Step 4: 更新 TASKLIST.md**

阶段 4 的「给用户分配角色：PUT /user/{userId}/roles + 回显接口 GET /user/{userId}/roles」打 `[x]`，按惯例附联调日期与结果说明（如"联调验证(2026-08-23)：10 项全部通过"）

- [ ] **Step 5: 提交**

```bash
git add docs/TASKLIST.md
git commit -m "Period4 #comment 用户分配角色联调通过，更新 TASKLIST"
```

---

## 自检记录

- **Spec 覆盖**：分配接口→Task 3/4，回显接口→Task 3/4，校验链→Task 3，事务→Task 3，DTO 校验→Task 4，数据层→Task 1，跨模块批量校验→Task 2，联调→Task 5；deleteUser 清理已在设计勘误中取消 ✓
- **占位符扫描**：无 TBD/TODO，每步含具体签名、SQL、断言与命令 ✓
- **类型一致性**：`assignRoles(String, List<String>)` / `getRoleIdsByUserId(String)` / `selectExistingRoleIds(List<String>)` / `batchInsert(@Param("list") List<UserRole>)` / `deleteByUserId(String)` / `selectRoleIdsByUserId(String)` 全计划引用一致 ✓
