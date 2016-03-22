package adapters;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.museupessoa.maf.assistenteentrevistas.fragments.Configurações;
import com.museupessoa.maf.assistenteentrevistas.fragments.Entrevistas;
import com.museupessoa.maf.assistenteentrevistas.fragments.Inicio;
import com.museupessoa.maf.assistenteentrevistas.fragments.Projetos;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private  String[] mTableTitles;

    public MyFragmentPagerAdapter(FragmentManager fm, String[] mTableTitles) {
        super(fm);
        this.mTableTitles = mTableTitles;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Inicio();
            case 1:
                return new Projetos();
            case 2:
                return new Entrevistas();
            case 3:
                return new Configurações();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.mTableTitles.length;
    }
    @Override
    public CharSequence getPageTitle(int position){
        return this.mTableTitles[position];
    }

}
