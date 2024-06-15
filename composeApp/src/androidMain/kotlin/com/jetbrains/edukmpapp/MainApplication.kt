package com.jetbrains.edukmpapp

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin { // Koin을 초기화하는 블록
            //this는 현재 코드가 실행되는 객체를 가르킴 @는 레이블 지정 명령어, 즉 현재 객체를 "MainApplication" 이름의 레이블로 기억하겠다.
            // androidContext는 Koin에서 제공하는 안드로이드 확장 기능으로, 안드로이드 애플리케이션의 컨텍스트를 Koin에 등록한다는 의미
            androidContext(this@MainApplication)
            androidLogger() //  Koin의 로깅 기능을 활성화
        }
        //initKoin함수는 두개의 매개변수를 가져야하는데 이미 기본값이 있기 때문에 아무 매개변수도 주지 않고싶으면 매개변수 정의부 "()"를 생략할 수 있다.
        //이렇게 함으로서 initKoin을 할때 추가 설정이 필요하면 넣어주면 되고 필요없으면 그냥 생략하면 되는 유연성 높은 로직이 완성됨.
    }
}