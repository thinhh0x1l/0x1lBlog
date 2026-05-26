package top.blogapi.model.vo;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MomentLikesAndLiked {
    Long id;
    Integer likes;
    boolean liked;

    public static MomentLikesAndLiked empty = singletonEmpty();
    private static MomentLikesAndLiked singletonEmpty(){
        if(MomentLikesAndLiked.empty == null){
            MomentLikesAndLiked momentLikesAndLiked = new MomentLikesAndLiked();
            momentLikesAndLiked.liked=false;
            momentLikesAndLiked.likes=0;
            return momentLikesAndLiked;
        }
        return MomentLikesAndLiked.empty;
    }


}
