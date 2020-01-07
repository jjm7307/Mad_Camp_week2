package com.example.mad_camp_week2.models;

import android.net.Uri;

//public class ModelFavs {
//    private String name,gender,birthday;
//
//    public ModelFavs(String name, String gender, String birthday) {
//        this.name = name;
//        this.gender = gender;
//        this.birthday = birthday;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(String birthday) {
//        this.birthday = birthday;
//    }
//
//
//}
public class ModelFavs  { // ImageCard에 대응
    private String id; // 2415356
    private String name; // "hyebin"
    private int age; // 24
    private String rawBirthDay; // "12/06/1997"
    private Uri uri; // 이미지 uri --> 인터넷 상의 이미지를 받아옴(피카소 url) or 해당 디비에서 uri로 받아왔다고 가정

    // Constructor
    public ModelFavs() {
    }

    //

    public ModelFavs (String name, int age, String rawBirthDay) {
        this.name = name;
        this.age = age;
        this.rawBirthDay = rawBirthDay;
    }
    //



    public ModelFavs (String id, String name, int age, String rawBirthDay) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.rawBirthDay = rawBirthDay;
    }

    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRawBirthDay() {
        return rawBirthDay;
    }

    public void setRawBirthDay(String rawBirthDay) {
        this.rawBirthDay = rawBirthDay;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
    // 현재 객체의 저장된 정보를 가지고 이중의 하나의 필드값을 가지고 랜덤하게 연산해서 리턴한다.
    // name or age or Birthday season
    public String getRandomDescription(){
        String resultString ="";
        String tmp="";
        int select = (int)(Math.random()*3); // 0~2 사이의 난수 생성

        // 이름은
        switch (select){
            case 0:
                tmp = getMaskName(); // 가운데 가려진 이름문자열을 반환
                resultString = "이름은 "+tmp+"입니다.";
                break;
            case 1:
                tmp = Integer.toString(getAge()); // 현재 객체의 나이를 숫자로 반환
                resultString = "나이는 "+tmp+"입니다.";
                break;
            case 2:
                tmp = getSeason(); // 현재 객체의 생일이 속한 계절을 연산해서 문자열로 반환
                resultString = tmp+"에 태어났습니다.";
                break;
        }
        return resultString;
    }
    // 현재 객체의 이름을 가운데 마스킹해서 반환, 모두 세 글자라고 가정
    public String getMaskName(){
        String rawName = this.name;
        String first = rawName.substring(0,1);
        String last = rawName.substring(2,3);
        return first+"*"+last ; // "조*빈" 포맷
    }

    //현재 객체의 생일을 계절로 반환
    public String getSeason(){
        String tmpBirth = this.rawBirthDay; // "mm/dd/yyyy" 형태
        String tmpArr[] = tmpBirth.split("/");
        int tmpMonth = Integer.parseInt(tmpArr[0].trim()); // month가 1부터 시작한다고 가정
        // 3~5봄 , 6~8여름, 9~11가을 12~2겨울
        if((3<=tmpMonth)&&(tmpMonth<6))
            return "봄";
        else if((6<=tmpMonth)&&(tmpMonth<9))
            return "여름";
        else if((9<=tmpMonth)&&(tmpMonth<12))
            return "가을";
        else if((12==tmpMonth)||((1<=tmpMonth)&&(tmpMonth<3)))
            return "겨울";
        else return "";
    }
}
