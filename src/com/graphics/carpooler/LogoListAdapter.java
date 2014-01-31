package com.graphics.carpooler;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activities.carpooler.R;

/**
 * Classe ArrayAdapter modifiee pour remplir la liste avec un tableau d'objets LogoList
 * @author Loic Lacomblez
 *
 */
public class LogoListAdapter extends ArrayAdapter<LogoList>
{
	private Context context; 
	private int layoutResourceId;    
	private LogoList data[];

	/**
	 * Constructeur de la classe
	 * @param context
	 * @param layoutResourceId
	 * @param data
	 */
	public LogoListAdapter(Context context, int layoutResourceId, LogoList[] data)
	{
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	/* Rempli la liste selon le template d'une LogoList */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		LogoListHolder holder;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new LogoListHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.image_row);
			holder.txtTitle = (TextView)row.findViewById(R.id.text_row);

			row.setTag(holder);
		}
		else
		{
			holder = (LogoListHolder)row.getTag();
		}

		LogoList element = data[position];
		holder.txtTitle.setText(element.getText());
		holder.imgIcon.setImageResource(element.getIcon());

		return row;
	}

	/* Fournit un cache contenant les elements a inserer sur la ligne courante.
	 * Permet un gain en performance
	 */
	static private class LogoListHolder
	{
		ImageView imgIcon;
		TextView txtTitle;
	}
}

