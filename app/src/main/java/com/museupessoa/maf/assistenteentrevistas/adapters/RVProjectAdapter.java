package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.main.Configuration;
import com.museupessoa.maf.assistenteentrevistas.main.Interviews;
import com.museupessoa.maf.assistenteentrevistas.main.Main;
import com.museupessoa.maf.assistenteentrevistas.main.Projects;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;


public  class RVProjectAdapter extends   RecyclerView.Adapter<RVProjectAdapter.ProjectViewHolder> {

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView projectName;
        private final  String TAG="AssistenteEntrevistas";

        ProjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_Project);
            projectName = (TextView)itemView.findViewById(R.id.ProjectName);

            /*cv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case 0:
                            //v.setBackgroundColor(Color.RED);
                            break;
                        case 1:
                            //v.setBackgroundColor(Color.WHITE);
                            v.destroyDrawingCache();
                            break;
                        case 2:
                           v.setBackgroundColor(Color.GRAY);
                            v.destroyDrawingCache();

                            break;
                    }
                    return true;
                }
            });*/
          /*  cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "11111111");
                }
            });


            cv.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View v, MotionEvent event) {
                    cv.setBackgroundColor(Color.RED);
                    return true;
                }
            });
            cv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                }
            });
            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cv.setBackgroundColor(Color.RED);
                    return false;
                }
            });
            cv.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    cv.setBackgroundColor(Color.BLUE);
                    return false;
                }
            });*/
        }

    }

    List<ProjectUnit> projects;
    public RVProjectAdapter(List<ProjectUnit> projects){
        this.projects =  projects;
    }
    @Override
    public RVProjectAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item_cv, parent, false);
        ProjectViewHolder pvh = new ProjectViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVProjectAdapter.ProjectViewHolder holder, int position) {
        holder.projectName.setText( projects.get(position).name);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }


}
