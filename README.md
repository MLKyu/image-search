# kabank-cote
This is a coding test project with most of the ViewPager, TabLayout, coroutines and retrofit, it was built for submission

## Built with
* Retrofit 2.9.0
* Coroutine
* Live Data
* View Model
* Repository Pattern
* MVVM architecture
* Androidx
* Viewbinding
* Material design components
* Kotlin
* Kotlin Flow

## Author
MLK

## Contributions
Coding Test for submission

## Review
+ Flo, Result
+ Monad 공부
+ 코루틴이면 그냥 데이터 받고 catch 로 예외처리 해도 되는데 왜 result 를?
+ Flo stream study 필요
+ DiffUtil content same 사용법
+ item same 이 같은 아이템인가를 체크하는데 equals 로 비교를 다하고 content same 에서 데이터를 다시비교해서 이상해 보암
+ view model, ui test 짜고, unit test 도
+ kotlin 기교 없음 dsl 이나 delegate properties 나 등등
+ pager handler 가 view model 단에 있는 것도 분리 필요
+ 테일 람다, 타입 추론 등등 코드 가독성 올릴 필요성 있음
+ repository 에 flow to livedata 랑 suspend function 이 섞임 동일 한 방식으로 통일해 사용성 올려라
+ insertItem 호출하는 곳 보니 viewModelScope 쓰는데 insertItem 에서 다시 코루틴 스코프를 생성함 그냥 withContext 로 dispatchers 만 바꾸면 될 것 같은데 다시 스코프를 생성한 이유는??
+ repository 사용성 통일은 꼭
+ 네트워크는 스트림 작업을 할께없어서.. flow 버리고 suspend function 사용 권장
+ zip 은 async await 으로 묶으면 됨
+ 가로세로 회전할꺼면 오류없나 체크
+ 테스트는 커버리지 다 채우지 말고 대충 1-2개씩
+ 대부분 상황에서 아키텍쳐는 많이 정형화되어 있으니 과제는 요번 구조 베이스로
