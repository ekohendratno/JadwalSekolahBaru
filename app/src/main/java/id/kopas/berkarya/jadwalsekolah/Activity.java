package id.kopas.berkarya.jadwalsekolah;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.kopas.berkarya.jadwalsekolah.database.DatabaseHelper;
import id.kopas.berkarya.jadwalsekolah.database.Jadwal;

public class Activity extends AppCompatActivity {
    public static Adapter adapter;
    public static DatabaseHelper db;
    public static int showPage = 0;
    public static EditText inputJadwal;
    public static EditText inputRuang;
    public static EditText inputHari;
    public static EditText inputMulai;
    public static EditText inputSelesai;
    static TabLayout tabLayout;
    private static BottomSheetDialog mBottomSheetDialog;
    final int dayNumber = (Calendar.getInstance()).get(Calendar.DAY_OF_WEEK);
    ViewPager viewPager;
    String weekDay;
    View bottomSheet;

    private static void showActionsDialog(final Context ctx, final int position) {
        CharSequence colors[] = new CharSequence[]{"Ubah", "Hapus"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Opsi pilihan");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //aksi

                    Activity a = new Activity();
                    a.showBottomSheetLayout(ctx, true, null, -1);

                } else {

                    Toast.makeText(ctx, "p:" + position, Toast.LENGTH_SHORT).show();
//
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        bottomSheet = findViewById(R.id.framelayout_bottom_sheet);

        if (viewPager != null) {
            adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment("Minggu");
            adapter.addFragment("Senin");
            adapter.addFragment("Selasa");
            adapter.addFragment("Rabu");
            adapter.addFragment("Kamis");
            adapter.addFragment("Jumat");
            adapter.addFragment("Sabtu");
            viewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomSheetLayout(view.getContext(), false, null, -1);
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(dayNumber - 1);
        tab.select();
    }

    public void showBottomSheetLayout(final Context ctx, final boolean shouldUpdate, final Jadwal jadwal, final int position) {


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ctx);
        final View bottomSheetLayout = layoutInflaterAndroid.inflate(R.layout.bottom_sheet_dialog, null);

        TextView dialogTitle = bottomSheetLayout.findViewById(R.id.dialog_jadwal);
        dialogTitle.setText(!shouldUpdate ? ctx.getString(R.string.lbl_new_jadwal_title) : ctx.getString(R.string.lbl_edit_jadwal_title));

        if (shouldUpdate && jadwal != null) {
            inputJadwal.setText(jadwal.getJadwal());
        }

        inputJadwal = bottomSheetLayout.findViewById(R.id.jadwal);
        inputRuang = bottomSheetLayout.findViewById(R.id.ruang);
        inputHari = bottomSheetLayout.findViewById(R.id.hari);
        inputMulai = bottomSheetLayout.findViewById(R.id.mulai);
        inputSelesai = bottomSheetLayout.findViewById(R.id.selesai);

        showPage(tabLayout.getSelectedTabPosition());
        inputHari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] charSequence = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                //builder.setTitle("Pilihan");
                builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showPage(i);
                    }
                });
                builder.create().show();
            }
        });

        inputMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputMulai.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        inputSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        inputSelesai.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(ctx);
        mBottomSheetDialog.setContentView(bottomSheetLayout);
        mBottomSheetDialog.show();


        (bottomSheetLayout.findViewById(R.id.button_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        (bottomSheetLayout.findViewById(R.id.button_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputJadwal.getText().toString())) {
                    Toast.makeText(Activity.this, "Masukkan Mata Pelajaran!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mBottomSheetDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && jadwal != null) {
                    // update note by it's id
                    updatePelajaran(
                            inputHari.getText().toString(),
                            inputJadwal.getText().toString(),
                            inputRuang.getText().toString(),
                            inputMulai.getText().toString(),
                            inputSelesai.getText().toString(),
                            "1",
                            position);
                } else {
                    // create new note
                    createPelajaran(
                            inputHari.getText().toString(),
                            inputJadwal.getText().toString(),
                            inputRuang.getText().toString(),
                            inputMulai.getText().toString(),
                            inputSelesai.getText().toString(),
                            "1");
                }


                viewPager.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                TabLayout.Tab tab = tabLayout.getTabAt(showPage);
                tab.select();
            }
        });

    }

    private void createPelajaran(String s, String s1, String s2, String s3, String s4, String s5) {
        long id = db.insertPelajaran(s, s1, s2, s3, s4, s5);
        Jadwal n = db.getPelajaran(id);
        if (n != null) {
            // adding new note to array list at 0 position
            //adapter.add(n);
            //adapter.add(n);
            // refreshing the list
            adapter.notifyDataSetChanged();
        }
    }

    private void updatePelajaran(String s, String s1, String s2, String s3, String s4, String s5, int position) {
        //long id = db.insertPelajaran(s,s1,s2,s3,s4,s5);
    }

    @SuppressLint("SetTextI18n")
    private void showPage(int i) {
        switch (i) {
            case 0:
                inputHari.setText("Minggu");

                showPage = 0;
                break;
            case 1:
                inputHari.setText("Senin");

                showPage = 1;
                break;
            case 2:
                inputHari.setText("Selasa");

                showPage = 2;
                break;
            case 3:
                inputHari.setText("Rabu");

                showPage = 3;
                break;
            case 4:
                inputHari.setText("Kamis");

                showPage = 4;
                break;
            case 5:
                inputHari.setText("Jumat");

                showPage = 5;
                break;
            case 6:
                inputHari.setText("Sabtu");

                showPage = 6;
                break;
        }
    }

    public static class Adapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String title) {
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return new PlaceholderFragment().newInstance(position + 1, mFragmentTitles.get(position));
        }

        @Override
        public int getCount() {
            return mFragmentTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";
        public static final String ARG_DATA = "ARG_DATA";
        int PAGE;
        String DAY;
        RecyclerView recyclerView;
        TextView emptyView;
        RecyclerViewAdapter adapter;
        private List<Jadwal> jadwalList = new ArrayList<>();

        public Fragment newInstance(int position, String day) {
            PlaceholderFragment f = new PlaceholderFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("ARG_PAGE", position);
            args.putString("ARG_DATA", day);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            PAGE = getArguments().getInt("ARG_PAGE");
            DAY = getArguments().getString("ARG_DATA");
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment, container, false);

            recyclerView = rootView.findViewById(R.id.recyclerView);
            emptyView = rootView.findViewById(R.id.empty_view);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

            Log.e("x:", PAGE + ":" + String.valueOf(DAY));

            db = new DatabaseHelper(getContext());
            jadwalList = db.getAllPelajaran(DAY);

            adapter = new RecyclerViewAdapter(jadwalList);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            if (adapter.getItemCount() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }


            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                    recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, final int position) {
                }

                @Override
                public void onLongClick(View view, int position) {
                    showActionsDialog(view.getContext(), position);
                }
            }));

            return rootView;
        }

    }

    //Untuk memasukkan data tiap item database list
    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.JadwalViewHolder> {
        private List<Jadwal> jadwalList;

        public RecyclerViewAdapter(List<Jadwal> jadwalList) {
            this.jadwalList = jadwalList;
        }

        @Override
        public JadwalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragmen_listitem, parent, false);

            return new JadwalViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(JadwalViewHolder holder, int position) {
            Jadwal jadwal = jadwalList.get(position);
            holder.time.setText(jadwal.getMulai());
            holder.title.setText(jadwal.getJadwal());
            holder.room.setText(jadwal.getRuang());
        }

        @Override
        public int getItemCount() {
            return jadwalList.size();
        }

        public class JadwalViewHolder extends RecyclerView.ViewHolder {
            public TextView time, title, room;

            public JadwalViewHolder(View itemView) {
                super(itemView);
                time = itemView.findViewById(R.id.time);
                title = itemView.findViewById(R.id.title);
                room = itemView.findViewById(R.id.room);
            }
        }
    }

}
