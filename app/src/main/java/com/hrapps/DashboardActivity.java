package com.hrapps;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.ArrayList;

import adapters.DashboardAdapter;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView recycler_view;
    RelativeLayout delete;
    int deletedPosition;
    private GestureDetector mGestureDetector;
    ArrayList<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        delete = (RelativeLayout) findViewById(R.id.delete);
        delete.setVisibility(View.GONE);
        delete.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:

                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        break;
                    case DragEvent.ACTION_DROP:
                        // Dropped, reassign View to ViewGroup
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        items.remove(deletedPosition);
                        owner.removeView(view);
                        delete.setVisibility(View.GONE);
                        recycler_view.getAdapter().notifyDataSetChanged();
                       /* RelativeLayout container = (RelativeLayout) v;
                        container.addView(view);*/
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        StaggeredGridLayoutManager gridManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(gridManager);
        recycler_view.setNestedScrollingEnabled(false);
        gridManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recycler_view.setHasFixedSize(true);
        recycler_view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.WHITE)
                .build());
        recycler_view.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .color(Color.WHITE)
                .build());
        for (int i = 0; i < 6; i++) {
            items.add("" + i);
        }
        recycler_view.setAdapter(new DashboardAdapter(items));
        /*recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                deletedPosition = position;

            }
        }));*/
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recycler_view.findChildViewUnder(e.getX(), e.getY());

                if (childView != null) {
                    deletedPosition = recycler_view.getChildPosition(childView);
                    delete.setVisibility(View.VISIBLE);
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(childView);
                    childView.startDrag(data, shadowBuilder, childView, 0);
                    //childView.setVisibility(View.INVISIBLE);
                }
            }
        });


        recycler_view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                /*if (childView != null && mGestureDetector.onTouchEvent(e)) {

                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(childView);
                        childView.startDrag(data, shadowBuilder, childView, 0);
                        childView.setVisibility(View.INVISIBLE);
                        deletedPosition = rv.getChildAdapterPosition(childView);
                        return true;
                    } else {
                        return false;
                    }

                }*/
                mGestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

}
