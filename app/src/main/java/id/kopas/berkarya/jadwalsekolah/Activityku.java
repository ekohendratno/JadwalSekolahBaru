package id.kopas.berkarya.jadwalsekolah;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.kopas.berkarya.jadwalsekolah.database.Jadwal;
import id.kopas.berkarya.jadwalsekolah.database.DatabaseHelper;

public class Activityku extends AppCompatActivity {

    private DatabaseHelper db;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activityku);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        db = new DatabaseHelper(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showJadwalDialog(false, null, -1);
            }
        });

        ArrayList<String> tabtitles = new ArrayList<>();
        tabtitles.add("Minggu");
        tabtitles.add("Senin");
        tabtitles.add("Selasa");
        tabtitles.add("Rabu");
        tabtitles.add("Kamis");
        tabtitles.add("Jumat");
        tabtitles.add("Sabtu");

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),tabtitles);
        viewPager.setAdapter(viewPagerAdapter);


        tabLayout.setupWithViewPager(viewPager);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> tabtitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager, ArrayList<String> tabtitles) {
            super(fragmentManager);
            this.tabtitles = tabtitles;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment .newInstance(tabtitles.get(position));
        }

        @Override
        public int getCount() {
            return tabtitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabtitles.get(position);
        }
    }

    public static class PlaceholderFragment extends Fragment {

        RecyclerView recyclerView;
        TextView emptyView;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String text) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, text);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(
                    R.layout.fragment, container, false);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            emptyView = (TextView) rootView.findViewById(R.id.empty_view);


            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

            return rootView;
        }
    }

    private void showJadwalDialog(final boolean shouldUpdate, final Jadwal jadwal, final int position) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.pelajaran_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Activityku.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputJadwal = view.findViewById(R.id.jadwal);
        final EditText inputRuang = view.findViewById(R.id.ruang);
        final EditText inputHari = view.findViewById(R.id.hari);
        final EditText inputMulai = view.findViewById(R.id.mulai);
        final EditText inputSelesai = view.findViewById(R.id.selesai);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputJadwal.getText().toString())) {
                    Toast.makeText(Activityku.this, "Masukkan Mata Pelajaran!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (jadwal != null) {
                    // update note by it's id
                    updatePelajaran(
                            inputJadwal.getText().toString(),
                            inputRuang.getText().toString(),
                            inputHari.getText().toString(),
                            inputMulai.getText().toString(),
                            inputSelesai.getText().toString(),
                            position);
                } else {
                    // create new note
                    createPelajaran(
                            inputJadwal.getText().toString(),
                            inputRuang.getText().toString(),
                            inputHari.getText().toString(),
                            inputMulai.getText().toString(),
                            inputSelesai.getText().toString());
                }
            }
        });
    }

    private void updatePelajaran(String s, String s1, String s2, String s3, String s4, int position) {
    }

    private void createPelajaran(String s, String s1, String s2, String s3, String s4) {
    }
}
