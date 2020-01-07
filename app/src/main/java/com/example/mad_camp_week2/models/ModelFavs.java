package com.example.mad_camp_week2.models;

import android.net.Uri;

public class ModelFavs {
    private String name,gender,birthday;
    private Integer my_age;

    public ModelFavs(String name, String gender, String birthday, Integer my_age) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.my_age = my_age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge(){
        String birth = this.birthday;
        String Arr_birth[] = birth.split("/");
        Integer birth_year = Integer.parseInt(Arr_birth[2].trim());
        if (this.my_age > birth_year) return "나보다 연상";
        else if(this.my_age == birth_year) return "나랑 동갑";
        else return "나보다 연하";
    }

    // 현재 객체의 이름을 가운데 마스킹해서 반환, 모두 세 글자라고 가정
    public String getMaskName(){
        String rawName = this.name;
        String first = rawName.substring(0,1);
        String last = rawName.substring(2,3);
        return first+"OO" ; // "조OO" 포맷
    }

    //현재 객체의 생일을 계절로 반환
    public String getSeason(){
        String tmpBirth = this.birthday; // "mm/dd/yyyy" 형태
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
