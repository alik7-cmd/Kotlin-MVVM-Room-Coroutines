# MVVM-Room-Kotlin
## What is MVVM
Each component has its own responsibilities to do their task

* The **View** receives user action and send it to the **ViewModel**, or listen live data stream from **ViewModel** and shows it to user.
* The **ViewModel** receives user actions from the **View** or provides data to View.
* The **Model** abstract the data sourec. We write our business logic here and both **View** and **ViewModel** uses that to stream data.
## Project configuration
We implement Android lifecycle, Room persistence library and Androidx here.

```
def materialDesignVersion = '1.0.0'
def constraintlayoutVersion = '1.1.3'
def appcompatVersion = '1.1.0'
def roomDbVersion = '1.1.1'
def lifecycleVersion = '1.1.1'
def espressoVersion = '3.2.0'
def junitVersion = '4.12'
def testRunnerVersion = '1.2.0'

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation "androidx.appcompat:appcompat:$appcompatVersion"
    implementation 'androidx.core:core-ktx:1.1.0'
    implementation "androidx.constraintlayout:constraintlayout:$constraintlayoutVersion"
    implementation "com.google.android.material:material:$materialDesignVersion"

    //dependency for architecture component
    implementation "android.arch.lifecycle:extensions:$lifecycleVersion"
    implementation "android.arch.lifecycle:viewmodel:$lifecycleVersion"

    //dependency for room persistance database
    implementation "android.arch.persistence.room:runtime:$roomDbVersion"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.android.material:material:1.0.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    kapt "android.arch.persistence.room:compiler:$roomDbVersion"

    testImplementation "junit:junit:$junitVersion"
    androidTestImplementation "androidx.test:runner:$testRunnerVersion"
    androidTestImplementation "androidx.test.espresso:espresso-core:$espressoVersion"
}
```
## Setting up Base Classes
I'm not using any dependency injection here right now. With keeping newbies in mind i published the application. I've added some base class here for making a generic adapter and also a base class for Activity.

``` kotlin
abstract class BaseActivity : AppCompatActivity (){
    abstract fun layoutRes(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
    }
}
```

Here are some important classes for making a generic adapter

``` kotlin
interface BaseRecyclerListener {
}
```

``` kotlin
abstract class BaseViewHolder <T,L : BaseRecyclerListener>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: T, listener: L?)
}
```

And finaly the **BaseRecyclerAdapter**

``` kotlin
abstract class BaseRecyclerAdapter<T, L : BaseRecyclerListener, VH : BaseViewHolder<T, L>>(val context: Context): RecyclerView.Adapter<VH>()
{

    internal var items: MutableList<T>? = null
    private var listener: L? = null
    private lateinit var layoutInflater: LayoutInflater
    private var position: Int = 0

    init {
        layoutInflater = LayoutInflater.from(context)
        items = ArrayList<T>()
    }

    abstract override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): VH

    override fun onBindViewHolder(p0: VH, @SuppressLint("RecyclerView") p1: Int) {
        val item = items!!.get(p1)
        p0.onBind(item, listener)
        position = p1
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    fun setItems(items: List<T>?) {
        if (items == null) {
            throw IllegalArgumentException("Cannot set `null` item to the Recycler adapter")
        }
        this.items!!.clear()
        this.items!!.addAll(items)
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<T>? {
        return this.items
    }

    fun add(item: T?) {
        if (item == null) {
            throw IllegalArgumentException("Cannot add null item to the Recycler adapter")
        }
        items!!.add(item)
        notifyItemInserted(items!!.size - 1)
    }

    fun addAll(items: List<T>?) {
        if (items == null) {
            throw IllegalArgumentException("Cannot add `null` items to the Recycler adapter")
        }
        this.items!!.addAll(items)
        notifyItemRangeInserted(this.items!!.size - items.size, items.size)
    }

    fun clear() {
        items!!.clear()
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        val position = items!!.indexOf(item)
        if (position > -1) {
            items!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun setListener(listener: L) {
        this.listener = listener
    }

    protected fun inflate(@LayoutRes layout: Int, parent: ViewGroup?, attachToRoot: Boolean): View {
        return layoutInflater.inflate(layout, parent, attachToRoot)
    }

    protected fun inflate(@LayoutRes layout: Int, parent: ViewGroup?): View {
        return inflate(layout, parent, false)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }
}
```
## Room Database
Room is one of the popular database library using SQLite made by Google.
IMO, Room has many advantages. It persists data over configuration changes, based on objected-oriented Modeling, supports migration, and it has really nice synergy with LiveData or Rxjava.

There are 3 major components in Room

* **Entity**: Represents a table within the database.
* **Dao**: Contains the methods used for accessing the database.
* **Database**: Contains the database holder and serves as the main access point for the underlying connection to your app's persisted, relational data.
## Configuring the Room Persistence
In **Project configuration** section i've already showed how to add Room in your project. Here is the **Entity**, **Dao** and **Database** class for configuring the Room database in project

``` kotlin
@Entity
data class Category(
    @PrimaryKey
    val id: String,
    val categoryName: String,
    val categoryDescription: String
)
```

``` kotlin
@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category")
    fun getAllCategory(): LiveData<List<Category>>

    @Insert
    fun insert(vararg category: Category)

    @Delete
    fun delete(category: Category)

    @Insert
    fun insertList(categoryList : List<Category>)

    @Update
    fun updateTodo(vararg category: Category)
}
```

``` kotlin
@Database(entities = arrayOf(Category::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Sample.db").allowMainThreadQueries()
                .build()
    }
}
```

Please note that you can also customize your column name in **Entity** class if you want. All you have to do is to add `@ColumnInfo(name = "desired_name")` before declaring a variable in Entity class. if you don't use this `@ColumnInfo` annotation then it is automatically ignored.
## Create Repository
This basically is a wrapper class which contains the business logic of our app. we will write all the database operation here so the person who is working on the UI does not have to worry about any database operations.

We have already created the **Repository** class here

``` kotlin
class Repository(private var application: Application) {
    var mDao: CategoryDao

    var allCategoryList: LiveData<List<Category>>? = null

    init {
        mDao = AppDatabase.getInstance(application).categoryDao()
        allCategoryList = mDao.getAllCategory()

    }

    fun getAllCategory(): LiveData<List<Category>>? {
        return allCategoryList
    }

    fun insertCategory(category: Category){
        InsertCategoryAsyncTask(mDao).execute(category)
    }

    fun insertCategoryList(categoryList: List<Category>){
        InsertCategoryListAsyncTask(mDao).execute(categoryList)
    }

    private class InsertCategoryAsyncTask internal constructor(private val categoryDao: CategoryDao) :
        AsyncTask<Category, Void, Void>() {

        override fun doInBackground(vararg category: Category): Void? {
            categoryDao.insert(category[0])
            return null
        }
    }

    private class InsertCategoryListAsyncTask internal constructor(private val categoryDao: CategoryDao) :
        AsyncTask<List<Category>, Void, Void>() {

        override fun doInBackground(vararg categoryList: List<Category>): Void? {
            categoryDao.insertList(categoryList[0])
            return null
        }
    }
}
```

In this Repository class, we will write all the DAO operations and access them in the UI through ViewModel.
##Create ViewModel Class
Now we'll create a ViewModel class. We already discussed above about the **ViewModel** class. for more information you can also check [here](https://developer.android.com/topic/libraries/architecture/viewmodel#targetText=The%20ViewModel%20class%20is%20designed,changes%20such%20as%20screen%20rotations.&targetText=The%20Android%20framework%20manages%20the,such%20as%20activities%20and%20fragments.)

We've already written our **CategoryViewModel** class 

``` kotlin
class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    var categoryLiveData: LiveData<List<Category>>? = null

    var mRepository: Repository? = null

    init {

        mRepository = Repository(application)

        categoryLiveData = mRepository?.getAllCategory()

    }

    internal fun getAllCategoryList(): LiveData<List<Category>>? {
        return categoryLiveData
    }

    internal fun saveSampleData() {
        mRepository?.insertCategoryList(getSampleList())
    }

    private fun getSampleList(): List<Category> {

        val listOfCategory = ArrayList<Category>()
        listOfCategory.add(Category(UUID.randomUUID().toString(), "Test 0", "Test 0"))
        listOfCategory.add(Category(UUID.randomUUID().toString(), "Test 1", "Test 1"))
        listOfCategory.add(Category(UUID.randomUUID().toString(), "Test 2", "Test 2"))

        return listOfCategory
    }

    fun insertCategory(category: Category){
        mRepository?.insertCategory(category)
    }

    fun checkValidity(name: String, description : String) : Boolean{

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description)){
            return true
        }

        return false
    }
}
```
## Use it in the UI
So we've finaly approached to our last stage. Now we will access the methods we have written in our ViewModel class in our Activity class.

``` kotlin
var mViewModel: CategoryViewModel? = null

mViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
 
mViewModel?.getAllCategoryList()?.observe(this, Observer { data ->
            // Do whatever you want to do with it right now, she is all your's now :p  
        })
```
For inserting the data in DB you can use something like this

``` kotlin
var mViewModel: CategoryViewModel? = null

mViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
mViewModel!!.insertCategory(
                    Category(
                        UUID.randomUUID().toString(),
                        alertDialogBuilder.et_name.text.toString(),
                        alertDialogBuilder.et_description.text.toString()
                    )
                )
```

> Here View is accessing the data from ViewModel class and ViewModel is accessing it from Repository class which has all the database operations.

Thanks for reading this :heart:. If I got something wrong please let me know about it. I would definitely love to improve more.
While writing this i took a huge inspiration from [this article](https://medium.com/mindorks/using-mvvm-android-jetpack-ebf1d9c1477c) published in https://medium.com

