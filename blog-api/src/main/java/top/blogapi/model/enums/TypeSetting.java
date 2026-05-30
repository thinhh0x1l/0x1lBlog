package top.blogapi.model.enums;

import lombok.Getter;

@Getter
public enum TypeSetting {
    TYPE_SITE_INFO(1),
    TYPE_BADGE(2),
    TYPE_INTRODUCTION(3),
    TYPE_MP3(36);

    private final int type;

    TypeSetting(int type){
        this.type = type;
    }
}
