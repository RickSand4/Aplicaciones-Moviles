package com.upiiz.practica_eohs_01;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowCompat;
import de.hdodenhof.circleimageview.CircleImageView;

final class Ui {
    static final int GREEN = Color.rgb(8, 127, 91), INK = Color.rgb(23, 39, 32),
            MUTED = Color.rgb(106, 119, 111), PALE = Color.rgb(221, 245, 230);
    private Ui() { }
    static int dp(Context c, float value) { return (int) (value * c.getResources().getDisplayMetrics().density + .5f); }
    static LinearLayout column(Context c) { LinearLayout v = new LinearLayout(c); v.setOrientation(LinearLayout.VERTICAL); return v; }
    static LinearLayout row(Context c) { LinearLayout v = new LinearLayout(c); v.setGravity(Gravity.CENTER_VERTICAL); return v; }
    static void pad(View v, int h, int vertical) { v.setPadding(dp(v.getContext(),h),dp(v.getContext(),vertical),dp(v.getContext(),h),dp(v.getContext(),vertical)); }
    static LinearLayout.LayoutParams size(Context c, int w, int h) {
        return new LinearLayout.LayoutParams(w < 0 ? w : dp(c,w), h < 0 ? h : dp(c,h));
    }
    static TextView text(Context c, String value, int sp, int color, boolean bold) {
        TextView t = new TextView(c); t.setText(value); t.setTextSize(sp); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    static GradientDrawable rounded(int color, int radius, Context c) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(c,radius)); return d;
    }
    static void clickable(View v, int color, int radius) {
        v.setBackground(new RippleDrawable(ColorStateList.valueOf(0x22087F5B), rounded(color,radius,v.getContext()), null));
        v.setClickable(true); v.setFocusable(true);
    }
    static TextView pill(Context c, String label, boolean active) {
        TextView t = text(c,label,14,active ? GREEN : MUTED,true); pad(t,16,10);
        t.setGravity(Gravity.CENTER); t.setMinHeight(dp(c,48));
        clickable(t,active ? PALE : 0xFFF1F4F2,24); return t;
    }
    static ImageView icon(Context c, int resource, String label, int color) {
        ImageView image = new ImageView(c); image.setImageResource(resource); image.setColorFilter(color);
        image.setContentDescription(label); pad(image,12,12); return image;
    }
    static ImageView action(LinearLayout row, int resource, String label, Runnable action) {
        ImageView image = icon(row.getContext(),resource,label,GREEN);
        clickable(image, Color.TRANSPARENT,24); image.setOnClickListener(v -> action.run());
        row.addView(image,size(row.getContext(),48,48)); return image;
    }
    static CircleImageView avatar(Context c, Contact contact, int size) {
        CircleImageView avatar = new CircleImageView(c);
        avatar.setImageBitmap(avatarBitmap(contact.name,contact.color));
        avatar.setContentDescription("Imagen de " + contact.name);
        avatar.setLayoutParams(size(c,size,size)); return avatar;
    }
    static Bitmap avatarBitmap(String name, int color) {
        Bitmap bitmap = Bitmap.createBitmap(128,128,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap); Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawColor(color); p.setColor(0x24FFFFFF); canvas.drawCircle(112,12,78,p);
        p.setColor(0x18000000); canvas.drawCircle(22,128,55,p);
        String[] words = name.split(" "); String initials = words[0].substring(0,1)
                + (words.length > 1 ? words[1].substring(0,1) : "");
        p.setColor(Color.WHITE); p.setTextSize(44); p.setTypeface(Typeface.create("sans-serif-medium",Typeface.NORMAL));
        p.setTextAlign(Paint.Align.CENTER); canvas.drawText(initials,64,80,p); return bitmap;
    }
    static Bitmap landscape(int seed) {
        Bitmap bitmap = Bitmap.createBitmap(320,480,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap); Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        int[] skies = {0xFFC8DCCD,0xFFEBD2B7,0xFFC8DCE8};
        c.drawColor(skies[seed % 3]); p.setColor(0xFFFFF0CB); c.drawCircle(220,110,43,p);
        p.setColor(0xFF78988A); c.drawOval(-120,210,360,650,p);
        p.setColor(0xFF3E7061); c.drawOval(100,265,520,680,p);
        p.setColor(0xFF244D43); c.drawOval(-140,350,390,720,p); return bitmap;
    }
    static void insets(AppCompatActivity activity) {
        EdgeToEdge.enable(activity);
        View root = activity.findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(root,(v,insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left,bars.top,bars.right,bars.bottom); return insets;
        });
        WindowCompat.getInsetsController(activity.getWindow(),root).setAppearanceLightStatusBars(true);
        WindowCompat.getInsetsController(activity.getWindow(),root).setAppearanceLightNavigationBars(true);
    }
}
