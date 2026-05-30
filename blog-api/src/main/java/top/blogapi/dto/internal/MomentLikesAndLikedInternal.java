package top.blogapi.dto.internal;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MomentLikesAndLikedInternal {
    Long id;
    Integer likes;
    boolean liked;

    public static MomentLikesAndLikedInternal empty = singletonEmpty();
    private static MomentLikesAndLikedInternal singletonEmpty(){
        if(MomentLikesAndLikedInternal.empty == null){
            MomentLikesAndLikedInternal momentLikesAndLikedInternal = new MomentLikesAndLikedInternal();
            momentLikesAndLikedInternal.liked=false;
            momentLikesAndLikedInternal.likes=0;
            return momentLikesAndLikedInternal;
        }
        return MomentLikesAndLikedInternal.empty;
    }


}
