package bthrust.eggonline.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;

/**
 * Created by win-3 on 1/15/2018.
 */

public class sqLiteDB extends SQLiteOpenHelper {
    public  static  final String DataBaseName = "eggOnlines";
    public static final int version = 5;
    public static final String Cart_Table = "Cart_Table";
    public static String KEY_ID = "ID";

    public static final String Cart_No = "Cart_No";
    public static final String Product_Name = "Product_Name";
    public static final String Product_Cast = "Product_Cast";
    public static final String Product_Image = "Product_Image";
    public static final String Category_ID = "Category_ID";
    public static final String Actual_Price = "Actual_Price";
    public static final String Total_Amount = "Total_Amount";

    Context myContect;
    public  sqLiteDB(Context context){
        super(context, DataBaseName, null, version);
        this.myContect = context;
    }

  /*  public sqLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String cart_Table = " create table "+ Cart_Table+ "("+KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Cart_No + " TEXT, " +
                Product_Name + " TEXT, " + Product_Cast + " TEXT, " + Product_Image + " BLOB, " + Category_ID + " TEXT, " + Actual_Price + " TEXT " +")";
        db.execSQL(cart_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Cart_Table);

    }

    public  Long insertCartData(MainBeen mainBeen){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Cart_No , mainBeen.getCartNumber());
        values.put(Product_Name ,mainBeen.getProductName());
        values.put(Product_Cast , mainBeen.getPrice());
        values.put(Product_Image , mainBeen.getImage());
        values.put(Category_ID , mainBeen.getCategoryID());
        values.put(Actual_Price , mainBeen.getActual_price());
        Log.e("2222=====>  " , mainBeen.getActual_price());
        return db.insert(Cart_Table , null , values);

    }


    public List<MainBeen> getAddCart(){
        SQLiteDatabase db = this.getWritableDatabase();
      List<MainBeen> cartList=new ArrayList<MainBeen>();
        Cursor mCursor = db.query(Cart_Table,
                null,null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                MainBeen con=new MainBeen();

                con.setCartNumber(mCursor.getString(mCursor.getColumnIndex(Cart_No)));
                con.setProductName(mCursor.getString(mCursor.getColumnIndex(Product_Name)));
                con.setPrice(mCursor.getString(mCursor.getColumnIndex(Product_Cast)));
                con.setImage(mCursor.getString(mCursor.getColumnIndex(Product_Image)));
                con.setCategoryID(mCursor.getString(mCursor.getColumnIndex(Category_ID)));
                con.setActual_price(mCursor.getString(mCursor.getColumnIndex(Actual_Price)));

                cartList.add(con);
            } while (mCursor.moveToNext());
        }
        return  cartList;

    }

    public  boolean updatecartTable(String categoryID ,  String productCast , String cartNo){
        ContentValues value = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        value.put(Cart_No , cartNo);
        value.put(Product_Cast , productCast);

        return db.update(Cart_Table , value , Category_ID + "='"+categoryID+"'",null)>0;
    }

    public boolean carDelete(String  catid) {
        Log.e("catid====>> " , catid);

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Cart_Table, Category_ID +  "='"+catid+"'", null) > 0;
    }
    public int deleteCartTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Cart_Table, null, null);
    }

}
