package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007")
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val studentAdapter = StudentAdapter(students)

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    addEvents()
  }

  private fun addEvents() {
    findViewById<Button>(R.id.btnAddNew).setOnClickListener(){
      addNewStudent()
    }
    findViewById<Button>(R.id.btnDelete).setOnClickListener(){
      deleteStudent()
    }
    findViewById<Button>(R.id.btnEdit).setOnClickListener(){
      editStudent()
    }
  }


  private fun editStudent() {
    val edtName = findViewById<EditText>(R.id.edtName)
    val edtMSSV = findViewById<EditText>(R.id.edtMSSV)

    val name = edtName.text.toString()
    val mssv = edtMSSV.text.toString()

    val student = students.find { it.studentId == mssv }
    if (student != null){
      val dialogEditView = layoutInflater.inflate(R.layout.dialog_edit_student, null)
      val edtNameDialog = dialogEditView.findViewById<EditText>(R.id.edtNameDialog)
      val edtMSSVDialog = dialogEditView.findViewById<EditText>(R.id.edtMSSVDialog)

      edtNameDialog.setText(student.studentName)
      edtMSSVDialog.setText(student.studentId)

      AlertDialog.Builder(this)
        .setTitle("Chỉnh sửa thông tin sinh viên")
        .setView(dialogEditView)
        .setPositiveButton("Cập nhật") { dialog, _ ->
          val name = edtNameDialog.text.toString()
          val mssv = edtMSSVDialog.text.toString()
          student.studentName = name
          student.studentId = mssv
          render()
          dialog.dismiss()
        }
        .setNegativeButton("Hủy") { dialog, _ ->
          dialog.dismiss()
        }
        .create()
        .show()
    }
  }

  private fun deleteStudent() {
    val edtName = findViewById<EditText>(R.id.edtName)
    val edtMSSV = findViewById<EditText>(R.id.edtMSSV)

    val name = edtName.text.toString()
    val mssv = edtMSSV.text.toString()

    val student = students.find { it.studentId ==  mssv}
    if(student != null){
      val removeStudent = student
      AlertDialog.Builder(this)
        .setTitle("Xoá sinh viên")
        .setMessage("Bạn có chắc chắn muốn xóa sinh viên này ra khỏi danh sách?")
        .setPositiveButton("Xóa") { dialog, _ ->
          students.remove(student)
          render()
          dialog.dismiss()
          Snackbar.make(findViewById(android.R.id.content), "Sinh viên đã bị xóa", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
              students.add(removeStudent) // Khôi phục sinh viên
              render()
            }
            .show()
        }
        .setNegativeButton("Hủy") { dialog, _ ->
          dialog.dismiss()
        }
        .create()
        .show()
    }
  }

  private fun addNewStudent() {
    val edtName = findViewById<EditText>(R.id.edtName)
    val edtMSSV = findViewById<EditText>(R.id.edtMSSV)

    val name = edtName.text.toString()
    val mssv = edtMSSV.text.toString()

    if(name.isEmpty() || mssv.isEmpty()){
      Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
    }else{
      students.add(StudentModel(name, mssv))
      render()
    }
  }

  private fun render() {
    val studentAdapter = StudentAdapter(students)

    val rv = findViewById<RecyclerView>(R.id.recycler_view_students)
    rv.layoutManager = LinearLayoutManager(this)
    rv.adapter = studentAdapter
  }

}