package com.example.memorygame.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorygame.Adapter.RecyclerViewAdapter;
import com.example.memorygame.ButtonList;
import com.example.memorygame.HandleStageButton;
import com.example.memorygame.Object.ImageRecycleViewObject;
import com.example.memorygame.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectObjectActivity extends AppCompatActivity implements HandleStageButton {

    List<ImageRecycleViewObject> selectedImage;
    RecyclerView recyclerView;
    List<Integer> images;
    LinearLayoutManager  linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    Button nextButton,escButton,replayButton;
    List<ShapeableImageView>buttons;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_object);
        this.images = ButtonList.getInstance().getImageList();
        this.buttons = Arrays.asList(findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9));
        this.nextButton = findViewById(R.id.nextButton);
        this.escButton = findViewById(R.id.escButton);
        this.replayButton = findViewById(R.id.replayButton);
        this.recyclerView = findViewById(R.id.recycleview);
        this.selectedImage = new ArrayList<>();
        this.linearLayoutManager = new LinearLayoutManager(SelectObjectActivity.this,LinearLayoutManager.HORIZONTAL,false);
        this.recyclerViewAdapter = new RecyclerViewAdapter(this,selectedImage);
        this.recyclerView.setLayoutManager(this.linearLayoutManager);
        this.recyclerView.setAdapter(this.recyclerViewAdapter);
        this.initialImageTab();

        this.nextButton.setOnClickListener(view -> handleNextButton(view));
        this.escButton.setOnClickListener(view->handleEscButton(view));
        this.replayButton.setOnClickListener(view->handleReplayButton(view));

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <K, V> Map<ShapeableImageView, Integer> zipToMap(List<ShapeableImageView> keys, List<Integer> values) {
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void selectedClick(Integer image, ShapeableImageView button){
        Drawable cross = getDrawable(R.drawable.cross);
        if (!this.selectedImage.stream().anyMatch(element->element.getImageId()==image)){
            button.setForeground(cross);
            button.setImageResource(image);
            ImageRecycleViewObject newObject = new ImageRecycleViewObject();
            newObject.setImageId(image);
            this.selectedImage.add(newObject);
            this.recyclerViewAdapter.notifyItemInserted(selectedImage.size()-1);
        }
        else {
            button.setForeground(null);
            button.setImageResource(image);
            Integer index = IntStream.range(0,this.selectedImage.size())
                    .filter(i->this.selectedImage.get(i).getImageId()==image)
                    .findFirst()
                    .orElseGet(null);
            ImageRecycleViewObject selectObject  = this.selectedImage
                    .stream()
                    .filter(element->element.getImageId()==image)
                    .findFirst()
                    .get();
            if (index!=null){
                this.selectedImage.remove(selectObject);
                this.recyclerViewAdapter.notifyItemRemoved(index);
                button.setImageResource(image);
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void initialImageTab(){
        Map <ShapeableImageView, Integer> buttonIntegerMap = zipToMap(this.buttons,this.images);
        buttonIntegerMap.entrySet().forEach(buttonElement->{
            Integer Image = buttonElement.getValue();
            ShapeableImageView button = buttonElement.getKey();
            button.setImageResource(Image);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                button.setOnClickListener(view -> selectedClick(Image,button));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void handleNextButton(View view) {
        Intent intent = new Intent(this,MatchColorActivity.class);
        intent.putParcelableArrayListExtra("selectedImages", (ArrayList<ImageRecycleViewObject>) this.selectedImage);
        startActivity(intent);
    }
    @Override
    public void handleReplayButton(View view) {
        Intent intent = new Intent(this,SelectObjectActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleEscButton(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        this.startActivity(intent);
    }
}