package com.museupessoa.maf.assistenteentrevistas.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.FotoActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.dialogs.SetLegendForFotoDialogFragment;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SwipeFotoAdapter extends PagerAdapter {
    private Context ctx;
    private LayoutInflater layoutInflater;
    List<String> fotos;
    String path;


    public SwipeFotoAdapter(Context context, List<String> fotos, String path){
        this.ctx = context;
        this.fotos = fotos;
        this.path = path;

    }

    public void updateData(List<String> fotos){
        this.fotos = fotos;
    }


    @Override
    public int getCount() {
        return fotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater =  (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_fotos,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.swipe_foto);
        TextView name = (TextView)item_view.findViewById(R.id.swipe_foto_name);
        TextView legend = (TextView)item_view.findViewById(R.id.swipe_foto_legend);
        imageView.setImageBitmap(getBitmapOfFoto(fotos.get(position)));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrAddLegendFoto(position);
            }
        });
        name.setText(fotos.get(position));
        name.setTextColor(Color.WHITE);
        legend.setText(getFotoLegend(position));
        legend.setTextColor(Color.rgb(200,200,200));
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout) object);
    }

    private Bitmap getBitmapOfFoto(String foto){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(this.path+"/Fotos/"+foto);
        return bitmap;
    }

    private void DeleteOrAddLegendFoto(final int position) {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                ctx);
        quitDialog.setTitle("O que?");

      quitDialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              FotoActivity.deleteFoto(position);

          }
      });
        quitDialog.setNegativeButton("Escrever legenda", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentManager fm = FotoActivity.fm;
                SetLegendForFotoDialogFragment fotoName = new SetLegendForFotoDialogFragment(position,getFotoLegend(position));
                fotoName.show(fm,"ChangeNameFotoDialogFragment");
            }
        });

        quitDialog.show();
    }
 private String getFotoLegend(int position){
     String legend = new String();
     File f = new File(path+"/Manifesto.xml");
     Document doc=null;
     if(f.exists()){
         try {
             doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
             NodeList res = doc.getElementsByTagName("photo");
             for(int i=0;i<res.getLength();i++){
                 if(res.item(i).getAttributes().getNamedItem("name").getTextContent().equals(fotos.get(position))){
                     legend = res.item(i).getAttributes().getNamedItem("legend").getTextContent();
                     break;
                 }
             }
         } catch (SAXException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (ParserConfigurationException e) {
             e.printStackTrace();
         }
     }
     return  legend;
 }

}
