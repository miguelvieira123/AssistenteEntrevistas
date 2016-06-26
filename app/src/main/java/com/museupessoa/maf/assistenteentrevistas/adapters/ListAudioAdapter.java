package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.ListenAudio;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteAudioDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.units.AudioUnit;

import java.util.HashMap;
import java.util.List;


public class ListAudioAdapter  extends BaseExpandableListAdapter  {
    private Context context;
    private List<AudioUnit> audioUnit;

   public HashMap<String, View> childviewReferences = new HashMap<String, View>();

    public ListAudioAdapter(Context context, List<AudioUnit> audioUnit) {
        this.context = context;
        this.audioUnit = audioUnit;
    }

    public  void deleteAudioFromList(int groupPosition,int childPosititon){
       this.audioUnit.get(groupPosition).audio.remove(childPosititon);
        this.audioUnit.get(groupPosition).time.remove(childPosititon);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.audioUnit.get(groupPosition).audio.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_audio, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.ListenAudioName);
        TextView time = (TextView)convertView.findViewById(R.id.ListenAudioCurrentAudioTime);
        txtListChild.setText(childText);
        time.setText(General.getStrTime(audioUnit.get(groupPosition).time.get(childPosition)));
        childviewReferences.put(Integer.toString(groupPosition) + Integer.toString(childPosition), convertView);
        ImageView delete = (ImageView)convertView.findViewById(R.id.ListenAudioDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListenAudio.groupPosToDel=groupPosition;
                ListenAudio.childPosToDel=childPosition;
                FragmentManager fm = ListenAudio.fm;
                DeleteAudioDialogFragment deleteAudio = new DeleteAudioDialogFragment();
                deleteAudio.show(fm,"DeleteAudioFile");
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       return this.audioUnit.get(groupPosition).audio.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.audioUnit.get(groupPosition).question;
    }

    @Override
    public int getGroupCount() {
        return this.audioUnit.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_audio, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
