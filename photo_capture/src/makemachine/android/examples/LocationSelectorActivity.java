package makemachine.android.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class LocationSelectorActivity extends Activity {

    protected static final String TAG = "MainActivity";
    private LinearLayout mIndexerLayout;
    private ListView mListView;
    private RelativeLayout mSectionToastLayout;
    private TextView mSectionToastText;
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private SectionIndexer mIndexer;
    private int lastSelectedPosition = -1;

   private ArrayList<String> mItems;
private ArrayList<SelectionModel> selectionModels;
   
   static class ViewHolder {
       protected TextView text;
       protected CheckBox checkbox;
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_selector_ui);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        initView();
    }

    private void initView() {
        mIndexerLayout = (LinearLayout) findViewById(R.id.indexer_layout);
        mListView = (ListView) findViewById(R.id.contacts_list);
        mSectionToastLayout = (RelativeLayout) findViewById(R.id.section_toast_layout);
        mSectionToastText = (TextView) findViewById(R.id.section_toast_text);
        for(int i = 0; i < alphabet.length(); i++) {
            TextView letterTextView = new TextView(this);
            letterTextView.setText(alphabet.charAt(i)+"");
            letterTextView.setTextSize(12f);
            letterTextView.setGravity(Gravity.CENTER);
            LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
            letterTextView.setLayoutParams(params);
            letterTextView.setPadding(4, 2, 4, 2);
            mIndexerLayout.addView(letterTextView);
            mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
        }

        String[] COUNTRIES = new String[]
                { "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
                "Eritrea", "Estonia", "Ethiopia", "Faeroe Islands",
                "Falkland Islands", "Fiji", "Finland", "Afghanistan", "Albania",
                "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
                "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
                "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
                "Bangladesh", "Barbados", "Belarus", "Belgium", "Monaco",
                "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar",
                "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
                "New Caledonia", "New Zealand", "Guyana", "Haiti",
                "Heard Island and McDonald Islands", "Honduras", "Hong Kong",
                "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
                "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
                "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
                "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
                "Liechtenstein", "Lithuania", "Luxembourg", "Nicaragua", "Niger",
                "Nigeria", "Niue", "Norfolk Island", "North Korea",
                "Northern Marianas", "Norway", "Oman", "Pakistan", "Palau",
                "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
                "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
                "French Southern Territories", "Gabon", "Georgia", "Germany",
                "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada",
                "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
                "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico",
                "Micronesia", "Moldova", "Bosnia and Herzegovina", "Botswana",
                "Bouvet Island", "Brazil", "British Indian Ocean Territory",
                "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone",
                "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
                "South Africa", "South Georgia and the South Sandwich Islands",
                "South Korea", "Spain", "Sri Lanka", "Sudan", "Suriname",
                "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
                "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
                "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
                "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
                "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States",
                "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
                "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
                "Virgin Islands", "Wallis and Futuna", "Western Sahara",
                "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso",
                "Burundi", "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada",
                "Cape Verde", "Cayman Islands", "Central African Republic", "Chad",
                "Chile", "China", "Reunion", "Romania", "Russia", "Rwanda",
                "Sqo Tome and Principe", "Saint Helena", "Saint Kitts and Nevis",
                "Saint Lucia", "Saint Pierre and Miquelon", "Belize", "Benin",
                "Bermuda", "Bhutan", "Bolivia", "Christmas Island",
                "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
                "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus",
                "Czech Republic", "Democratic Republic of the Congo", "Denmark",
                "Djibouti", "Dominica", "Dominican Republic",
                "Former Yugoslav Republic of Macedonia", "France", "French Guiana",
                "French Polynesia", "Macau", "Madagascar", "Malawi", "Malaysia",
                "Maldives", "Mali", "Malta", "Marshall Islands", "Yemen",
                "Yugoslavia", "Zambia", "Zimbabwe" };
        mItems = new ArrayList<String>(Arrays.asList(COUNTRIES));
        Collections.sort(mItems);
        
        selectionModels = new ArrayList<SelectionModel>();
        selectionModels.add(new SelectionModel("India"));
        selectionModels.add(new SelectionModel("French Polynesia"));
        selectionModels.add(new SelectionModel("Yemen"));
        selectionModels.add(new SelectionModel("Malaysia"));
        selectionModels.add(new SelectionModel("Congo"));
        selectionModels.add(new SelectionModel("Romania"));
        selectionModels.add(new SelectionModel("Belize"));
        selectionModels.add(new SelectionModel("Yemen"));
        selectionModels.add(new SelectionModel("Zambia"));
        selectionModels.add(new SelectionModel("Zimbabwe"));
        selectionModels.add(new SelectionModel("Mali"));
        selectionModels.add(new SelectionModel("Macau"));
        selectionModels.add(new SelectionModel("Madagascar"));
        selectionModels.add(new SelectionModel("Djibouti"));
        selectionModels.add(new SelectionModel("Czech Republic"));
        selectionModels.add(new SelectionModel("Dominica"));
        selectionModels.add(new SelectionModel("Cyprus"));
        selectionModels.add(new SelectionModel("France"));
        selectionModels.add(new SelectionModel("Croatia"));
        selectionModels.add(new SelectionModel("Rwanda"));
        selectionModels.add(new SelectionModel("Chile"));
        selectionModels.add(new SelectionModel("Dominica"));
        selectionModels.add(new SelectionModel("Cyprus"));
        selectionModels.add(new SelectionModel("France"));
        selectionModels.add(new SelectionModel("Croatia"));
        selectionModels.add(new SelectionModel("Rwanda"));
        selectionModels.add(new SelectionModel("Chile"));
        Collections.sort(selectionModels);
        
        InteractiveArrayAdapter adapter1 = new InteractiveArrayAdapter(this, selectionModels);
        mListView.setAdapter(adapter1);
        mIndexer = (SectionIndexer) adapter1;
        
        //ContentAdapter adapter = new ContentAdapter(this, android.R.layout.simple_list_item_multiple_choice, mItems);
        //mIndexer = (SectionIndexer) adapter;
        //mListView.setAdapter(adapter);
        //mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mIndexerLayout.setOnTouchListener(mOnTouchListener);

        final EditText searchText = (EditText) findViewById(R.id.EditText01);

        /*searchText.addTextChangedListener(new TextWatcher()
        {
            private int textlength;
            private ArrayList<String> array_sort= new ArrayList<String>();

            public void afterTextChanged(Editable s) {
                // Abstract Method of TextWatcher Interface.
            }
            public void beforeTextChanged(CharSequence s,
                    int start, int count, int after) {
                // Abstract Method of TextWatcher Interface.
                SparseBooleanArray checkedItem = mListView.getCheckedItemPositions();
                Integer key = checkedItem.keyAt(2);
                Log.d("", key.toString());
            }
            public void onTextChanged(CharSequence s,
                    int start, int before, int count) {
                textlength = searchText.getText().length();
                array_sort.clear();

                for (int i = 0; i < mItems.size(); i++) {
                    if (textlength <= mItems.get(i).length()) {
                        if(searchText.getText().toString().equalsIgnoreCase(
                                (String)
                                mItems.get(i).subSequence(0,
                                        textlength))) {
                            array_sort.add(mItems.get(i));
                        }
                    }
                } 
                mListView.setAdapter(new ContentAdapter(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, array_sort));
            }
        });*/
        
        searchText.addTextChangedListener(new TextWatcher()
        {
            private int textlength;
            private ArrayList<SelectionModel> array_sort= new ArrayList<SelectionModel>();

            public void afterTextChanged(Editable s) {
                // Abstract Method of TextWatcher Interface.
            }
            public void beforeTextChanged(CharSequence s,
                    int start, int count, int after) {
                // Abstract Method of TextWatcher Interface.
               
            }
            public void onTextChanged(CharSequence s,
                    int start, int before, int count) {
                textlength = searchText.getText().length();
                array_sort.clear();

                for (int i = 0; i < selectionModels.size(); i++) {
                    if (textlength <= selectionModels.get(i).getName().length()) {
                        if(searchText.getText().toString().equalsIgnoreCase(
                                (String)
                                selectionModels.get(i).getName().subSequence(0,
                                        textlength))) {
                            array_sort.add(selectionModels.get(i));
                        }
                    }
                } 
                mListView.setAdapter(new InteractiveArrayAdapter(LocationSelectorActivity.this, array_sort));
                if(array_sort.size() == selectionModels.size()) {
                    mIndexerLayout.setVisibility(View.VISIBLE);
                } else {
                    mIndexerLayout.setVisibility(View.GONE);
                }
                
            }
        });
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float alphabetHeight = mIndexerLayout.getHeight();
            float y = event.getY();
            int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));
            if (sectionPosition < 0) {
                sectionPosition = 0;
            } else if (sectionPosition > 26) {
                sectionPosition = 26;
            }
            if(lastSelectedPosition != sectionPosition) {
                if(-1 != lastSelectedPosition){
                    ((TextView) mIndexerLayout.getChildAt(lastSelectedPosition)).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                lastSelectedPosition = sectionPosition;
            }
            String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));
            int position = mIndexer.getPositionForSection(sectionPosition);
            TextView textView = (TextView) mIndexerLayout.getChildAt(sectionPosition);

            if(textView.getText().equals(sectionLetter)) {
                Log.d("", "");
                textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
            }
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                mSectionToastLayout.setVisibility(View.VISIBLE);
                mSectionToastText.setText(sectionLetter);
                mListView.setSelection(position);
                break;
            case MotionEvent.ACTION_MOVE:
                mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
                textView.setBackgroundColor(getResources().getColor(R.color.letter_bg_color));
                mSectionToastLayout.setVisibility(View.VISIBLE);
                mSectionToastText.setText(sectionLetter);
                mListView.setSelection(position);
                break;
            case MotionEvent.ACTION_UP:
                //mIndexerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mSectionToastLayout.setVisibility(View.GONE);
            default:
                mSectionToastLayout.setVisibility(View.GONE);
                break;
            }
            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        return super.onOptionsItemSelected(item);
    }

    private class ContentAdapter extends ArrayAdapter<String> implements SectionIndexer {

        private List<String> mList;
        private Context mContext;
        
        public ContentAdapter(Context context, int textViewResourceId,
                List<String> objects) {
            super(context, textViewResourceId, objects);
            mList = objects;
            mContext = context;
        }

        @Override
        public int getPositionForSection(int section) {
            // If there is no item for current section, previous section will be selected
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(alphabet.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[alphabet.length()];
            for (int i = 0; i < alphabet.length(); i++)
                sections[i] = String.valueOf(alphabet.charAt(i));
            return sections;
        }
    }
    
    public class InteractiveArrayAdapter extends ArrayAdapter<SelectionModel> implements SectionIndexer {

        private final List<SelectionModel> list;
        private final Activity context;

        public InteractiveArrayAdapter(Activity context, List<SelectionModel> list) {
          super(context, R.layout.rowbuttonlayout, list);
          this.context = context;
          this.list = list;
        }

       
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          View view = null;
          if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.rowbuttonlayout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.label);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
            viewHolder.checkbox
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                  @Override
                  public void onCheckedChanged(CompoundButton buttonView,
                      boolean isChecked) {
                      SelectionModel element = (SelectionModel) viewHolder.checkbox
                        .getTag();
                    element.setSelected(buttonView.isChecked());

                  }
                });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
          } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
          }
          ViewHolder holder = (ViewHolder) view.getTag();
          holder.text.setText(list.get(position).getName());
          holder.checkbox.setChecked(list.get(position).isSelected());
          return view;
        }
        
        @Override
        public int getPositionForSection(int section) {
            // If there is no item for current section, previous section will be selected
            for (int i = section; i >= 0; i--) {
                for (int j = 0; j < getCount(); j++) {
                    if (i == 0) {
                        // For numeric section
                        for (int k = 0; k <= 9; k++) {
                            if (StringMatcher.match(String.valueOf(getItem(j).getName().charAt(0)), String.valueOf(k)))
                                return j;
                        }
                    } else {
                        if (StringMatcher.match(String.valueOf(getItem(j).getName().charAt(0)), String.valueOf(alphabet.charAt(i))))
                            return j;
                    }
                }
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            return 0;
        }

        @Override
        public Object[] getSections() {
            String[] sections = new String[alphabet.length()];
            for (int i = 0; i < alphabet.length(); i++)
                sections[i] = String.valueOf(alphabet.charAt(i));
            return sections;
        }
    }
    
    private class SelectionModel implements Comparable<SelectionModel> {
        private String mName;
        private boolean selected;

        public SelectionModel(String name) {
            mName = name;
          selected = false;
        }

        public String getName() {
          return mName;
        }
       
        public boolean isSelected() {
          return selected;
        }

        public void setSelected(boolean selected) {
          this.selected = selected;
        }
        
        public int compareTo(SelectionModel model) {
            int result = mName.compareTo(model.getName());
            return result;
        }
    }
}