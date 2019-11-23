package staddle.com.staddle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import staddle.com.staddle.R;
import staddle.com.staddle.bean.MenuListModule;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MenuListModule> menuListModuleArrayList = new ArrayList<>();
    private OnItemClickListener listener;
    int quantity = 1;
    private String count1;

    public MenuListAdapter(Context mContext, ArrayList<MenuListModule> menuListModuleArrayList) {
        this.mContext = mContext;
        this.menuListModuleArrayList = menuListModuleArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_menu_adapter, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MenuListModule menuListModule = menuListModuleArrayList.get(position);
        holder.bind(menuListModule, listener);
        final String name = menuListModule.getName();
        holder.tv_quantity.setText(menuListModule.getCount() + "");
        holder.txt_menu_name.setText(name);

        holder.img_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = menuListModule.getCount();
                count = count + 1;
                count1 = Integer.valueOf(count).toString();
                //  quantity++;
                holder.tv_quantity.setText(" " + count1);
                menuListModule.setCount(count);

                notifyDataSetChanged();
            }
        });
        holder.img_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = menuListModule.getCount();
                if (/*count != 1 &&*/ count > 1) {
                    count = count - 1;
                    count1 = Integer.valueOf(count).toString();
                    holder.tv_quantity.setText(count1);
                    menuListModule.setCount(count);
                    //   quantity--;
                    //holder.tv_quantity.setText("" + quantity);
                }
                if (count == 1) {
                    holder.img_decrease.setClickable(false);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuListModuleArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv_menu;
        ImageView img_increase, img_decrease;
        TextView txt_menu_name, tv_quantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_menu_name = itemView.findViewById(R.id.txt_menu_name);
            img_increase = itemView.findViewById(R.id.img_increase);
            img_decrease = itemView.findViewById(R.id.img_decrease);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            // iv_menu = itemView.findViewById(R.id.iv_menu);
        }
        void bind(final MenuListModule menuListModule, final OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(menuListModule);
                }
            });
        }
    }

    public void setOnItemClickListener(MenuListAdapter.OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;

    }

    public interface OnItemClickListener {
        void OnItemClick(MenuListModule menulistmodule);
    }
}
