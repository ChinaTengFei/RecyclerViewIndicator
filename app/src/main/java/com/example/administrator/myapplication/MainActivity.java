package com.example.administrator.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView rvMain;
    private RAdapter adapter;
    private CustomView viewById;

    static class TViewHolder extends RecyclerView.ViewHolder{

        private final View itemView;

        public TViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
    static class LineHodler extends RecyclerView.ViewHolder{
        private final View itemView;

        public LineHodler(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvMain = ((RecyclerView) findViewById(R.id.rv_main));
        final LinearLayoutManager layout = new LinearLayoutManager(this);
        rvMain.setLayoutManager(layout);
        viewById = ((CustomView) findViewById(R.id.cv_view));

        adapter = new RAdapter();
        rvMain.setAdapter(adapter);
        rvMain.post(new Runnable() {
            @Override
            public void run() {

                final View viewByPosition = layout.findViewByPosition(5);
                viewById.setCallBack(new CustomView.CallBack() {
                    int currentLineIndex= 5;
                    @Override
                    public void ViewOffset(int top) {
                        int measuredHeight = viewByPosition.getMeasuredHeight();
                        int i = measuredHeight / top;
                        if(top>i+measuredHeight/2){

                            int targetPointY =top/measuredHeight;
                            adapter.notifyItemMoved(currentLineIndex,targetPointY);

                            final View viewByPosition = layout.findViewByPosition(targetPointY);
                            if (viewByPosition != null) {

                                Log.i(TAG, "ViewOffset: "+viewByPosition.getClass().getName());
                            }
                            Log.d(TAG, "ViewOffset()"+top+"===="+(measuredHeight/2)+"===="+currentLineIndex+"==="+measuredHeight/2);
                            currentLineIndex= targetPointY;
                        }
                    }
                });
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button:
                adapter.notifyItemRemoved(4);
                break;
            case R.id.button2:
                adapter.notifyItemMoved(4,3);break;
            case R.id.button3:
                adapter.notifyItemMoved(3,2);break;
        }
    }

    class RAdapter extends RecyclerView.Adapter{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if(viewType ==1){

                    View textView = new View(MainActivity.this);
                    textView.setBackgroundColor(Color.RED);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                    return new LineHodler(textView);
                }
                TextView textView = new TextView(MainActivity.this);
                return new TViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof TViewHolder)
                ((TextView) ((TViewHolder) holder).itemView).setText(position+"");
        }

        @Override
        public int getItemCount() {
            return 200;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==5){
                return 1;
            }
            return super.getItemViewType(position);
        }
    }
}