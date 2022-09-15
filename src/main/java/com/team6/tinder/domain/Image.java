package com.team6.tinder.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Image extends com.team6.tinder.domain.Timestamped {

    private String key;
    private String path;


    @Builder
    public Image(String key, String path) {
        this.key = key;
        this.path = path;
    }


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name="post_id", nullable = false)
//    private Post post;
//
//    @Column(nullable = false)
//    private String imgURL;

//    public Image(Post post, String imageUrl){
//        URLvalidator.isValidURL(imageUrl);
//        ValidateObject.postValidate(post);
//        this.post=post;
//        this.imgURL = imageUrl;
//    }
}
