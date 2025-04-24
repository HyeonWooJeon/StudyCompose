package com.example.project2


import androidx.room.PrimaryKey
import io.realm.RealmObject
import io.realm.annotations.Required
import org.bson.types.ObjectId


open class RealmUser : RealmObject() {
    //PK id로 지정
    @PrimaryKey
    var id: ObjectId = ObjectId() // ObjectId 타입으로 기본 키 설정
    //내부 컬럼
    @Required
    var name: String = ""
    var age: Int = 0
    var pw: String = ""
}
