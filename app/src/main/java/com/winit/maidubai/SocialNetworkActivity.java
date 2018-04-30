package com.winit.maidubai;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SocialNetworkActivity extends BaseActivity {
    ImageView ivfacebook,ivtwitter,ivinstagram,ivyoutube;
    LinearLayout lSocialNetworkActivity;
    Intent intent;
    @Override
    public void initialise() {
        lSocialNetworkActivity= (LinearLayout) inflater.inflate(R.layout.activity_social_network,null);
        lSocialNetworkActivity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(lSocialNetworkActivity);
        setTypeFaceNormal(lSocialNetworkActivity);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.socialnetwork));
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setImageResource(R.drawable.menu_white);
        tvCancel.setVisibility(View.INVISIBLE);
        tvCancel.setClickable(false);
        tvCancel.setText(getResources().getString(R.string.save));
        setStatusBarColor();
        initialiseControls();
        ivfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* intent = new Intent(SocialNetworkActivity.this, FacebookWebViewActivity.class);
                intent.putExtra("url", "http://www.facebook.com/maidubaiwater");
                startActivity(intent);*/
                newFacebookIntent(SocialNetworkActivity.this.getPackageManager(),"https://www.facebook.com/maidubaiwater");
            }
        });

        ivtwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent = new Intent(SocialNetworkActivity.this, FacebookWebViewActivity.class);
                intent.putExtra("url", "http://www.twitter.com/maidubaiwater");
                startActivity(intent);*/


                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=maidubaiwater"));
                    startActivity(intent);

                }catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/#!/maidubaiwater")));
                }
            }
        });

        ivyoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent = new Intent(SocialNetworkActivity.this, FacebookWebViewActivity.class);
                intent.putExtra("url", "http://www.youtube.com/user/maidubaiwater");
                startActivity(intent);*/

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/user/maidubaiwater"));
                startActivity(intent);
            }
        });

        ivinstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* intent = new Intent(SocialNetworkActivity.this, FacebookWebViewActivity.class);
                intent.putExtra("url", "http://www.instagram.com/maidubaiwater");
                startActivity(intent);*/

                Uri uri = Uri.parse("http://instagram.com/_u/maidubaiwater");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/maidubaiwater")));
                }
            }
        });
    }

    @Override
    public void initialiseControls() {
        ivfacebook=(ImageView)lSocialNetworkActivity.findViewById(R.id.ivFacebook);
        ivtwitter=(ImageView)lSocialNetworkActivity.findViewById(R.id.ivTwitter);
        ivinstagram=(ImageView)lSocialNetworkActivity.findViewById(R.id.ivInstagram);
        ivyoutube=(ImageView)lSocialNetworkActivity.findViewById(R.id.ivYouTube);
    }

    public void newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.winit.maidubai", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } /*catch (PackageManager.NameNotFoundException ignored) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/maidubaiwater")));
        }*/catch (Exception ignored) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/maidubaiwater")));
        }

    }

    @Override
    public void loadData() {

    }
}
