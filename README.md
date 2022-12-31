# nebula

Minecraft item customization plugin for spigot. Use [nebulism](https://nebulism.netlify.app/) for easy use.

English README can be found [here](https://github.com/XTHEIA/nebula/wiki).

spigot을 지원하는 마인크래프트 아이템 커스텀 플러그인입니다.
쉬운 사용을 위한 [웹 에디터](https://nebulism.netlify.app/)를 지원합니다.

---

## 편집할 수 있는 값

|        값        |   자료형    |     범위      |        비고         |    예시    | [Nebulism](https://nebulism.netlify.app/) |
|:---------------:|:--------:|:-----------:|:-----------------:|:--------:|:-----------------------------------------:|
|       이름        |   문자열    |             |     색상 적용 가능      |          |                     O                     |
|       설명        |   문자열    |             | 색상 적용 가능, 여러 줄 지원 |          |                     O                     |
|       종류        | Material | 모든 Material |                   | 다이아몬드 도끼 |                     O                     |
|     타격 피해량      |    실수    |     0 ~     |                   |          |                     O                     |
|     치명타 확률      |    실수    | 0.00 ~ 1.00 |                   |          |                     O                     |
|     치명타 피해량     |    실수    |     0 ~     |                   |          |                     O                     |
|       내구도       |    정수    |     1 ~     |                   |          |                     O                     |
|     부서지지 않음     |    논리    | True, False |                   |          |                     O                     |
| CustomModelData |    정수    |  1 ~ 25565  |                   |          |                     X                     |



### 아이템 제작 방법
1. 인게임 명령어
2. 플러그인 폴더 내 파일 수동 추가,수정
3. 전용 아이템 프리셋 파일 제작 툴