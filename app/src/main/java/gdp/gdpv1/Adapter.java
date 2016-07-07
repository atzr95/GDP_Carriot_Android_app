package gdp.gdpv1;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    List<Data> list= new ArrayList<>();

    public Adapter (List<Data> list)
    {
            this.list=list;
    }

    @Override
    public int getItemCount() {
       return list.size();
    }


    public Data getItem(int i) {
        return list.get(i);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerlist_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.Data=getItem(position);
        holder.listItemNameView.setText(list.get(position).getName());
        holder.data.setText(list.get(position).getData());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(position==0) {
                    Intent intent = new Intent(v.getContext(),Graph.class);
                    v.getContext().startActivity(intent);
                }
                if(position==1) {
                    Intent intent = new Intent(v.getContext(),HumGraph.class);
                    v.getContext().startActivity(intent);
                }
                if(position==2) {
                    Intent intent = new Intent(v.getContext(),SoilGraph.class);
                    v.getContext().startActivity(intent);
                }
                if(position==3) {
                    Toast.makeText(v.getContext(),"no graph" ,Toast.LENGTH_SHORT).show();
                }
                if(position==4){
                    Intent intent = new Intent(v.getContext(),LightGraph.class);
                    v.getContext().startActivity(intent);
                }
                if(position==5) {
                    Intent intent = new Intent(v.getContext(),WaterGraph.class);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView listItemNameView;
        TextView data;
        Data Data;
        public MyViewHolder(View itemView) {
            super(itemView);
            listItemNameView= (TextView) itemView.findViewById(R.id.listitem_name);
            data=(TextView) itemView.findViewById(R.id.data);
        }

    }
}
