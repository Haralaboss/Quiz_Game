package com.sav.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sav.quizgame.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding: ActivityQuizBinding

    val database = FirebaseDatabase.getInstance("https://quizgame-1572a-default-rtdb.europe-west1.firebasedatabase.app/")
    val databaseReference = database.reference.child("questions").child("all")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0

    lateinit var timer : CountDownTimer
    private val totalTime = 30000L
    var timerContinue = false
    var leftTime = totalTime

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference.child("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        gameLogic()

        //Buttons

        quizBinding.buttonNext.setOnClickListener {

            resetTimer()
            gameLogic()
            restoreOptions()

        }

        quizBinding.buttonFinish.setOnClickListener {

            sendScore()

        }

        //Answers

        quizBinding.textViewA.setOnClickListener {

            pauseTimer()
            disableClickableOfOption()
            userAnswer = "a"
            if (correctAnswer == userAnswer){

                quizBinding.textViewA.setBackgroundResource(R.drawable.answer_shape_correct)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewA.setBackgroundResource(R.drawable.answer_shape_wrong)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

        }

        quizBinding.textViewB.setOnClickListener {
            pauseTimer()
            disableClickableOfOption()
            userAnswer = "b"
            if (correctAnswer == userAnswer){

                quizBinding.textViewB.setBackgroundResource(R.drawable.answer_shape_correct)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewB.setBackgroundResource(R.drawable.answer_shape_wrong)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

        }

        quizBinding.textViewC.setOnClickListener {
            pauseTimer()
            disableClickableOfOption()
            userAnswer = "c"
            if (correctAnswer == userAnswer){

                quizBinding.textViewC.setBackgroundResource(R.drawable.answer_shape_correct)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewC.setBackgroundResource(R.drawable.answer_shape_wrong)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

        }

        quizBinding.textViewD.setOnClickListener {
            pauseTimer()
            disableClickableOfOption()
            userAnswer = "d"
            if (correctAnswer == userAnswer){

                quizBinding.textViewD.setBackgroundResource(R.drawable.answer_shape_correct)
                userCorrect++
                quizBinding.textViewCorrect.text = userCorrect.toString()
            }
            else{
                quizBinding.textViewD.setBackgroundResource(R.drawable.answer_shape_wrong)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }

        }

    }

    //-------------------- Game logic --------------------------

    private fun gameLogic(){

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount) {

                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD

                    quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility = View.VISIBLE
                    quizBinding.linearLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()
                }
                else{

                    val dialogMessage = AlertDialog.Builder(this@QuizActivity, R.style.QuizDialogTheme)


                    dialogMessage.setTitle("You finished!")
                    dialogMessage.setMessage("Congratulations! Do you wish to save your score?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("Save Result"){dialogWindow, position ->

                        sendScore()

                    }
                    dialogMessage.setNegativeButton("Menu"){dialogWindow, position ->

                        val intent = Intent(this@QuizActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    dialogMessage.create().show()
                }

                questionNumber++

            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()

            }


        })
    }

    //-----------------------------------

    fun findAnswer(){
        when(correctAnswer){
            "a" -> quizBinding.textViewA.setBackgroundResource(R.drawable.answer_shape_correct)
            "b" -> quizBinding.textViewB.setBackgroundResource(R.drawable.answer_shape_correct)
            "c" -> quizBinding.textViewC.setBackgroundResource(R.drawable.answer_shape_correct)
            "d" -> quizBinding.textViewD.setBackgroundResource(R.drawable.answer_shape_correct)
        }

    }

    fun disableClickableOfOption(){
        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false



    }

    fun restoreOptions(){

        quizBinding.textViewA.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewB.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewC.setBackgroundResource(R.drawable.answer_shape)
        quizBinding.textViewD.setBackgroundResource(R.drawable.answer_shape)

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true
    }


    // Timer methods

    private fun startTimer(){

        timer = object : CountDownTimer(leftTime, 1000){
            override fun onTick(millisUntilFinished: Long) {
                leftTime = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuestion.text = "Sorry, Time is up! Continue with next question."
                disableClickableOfOption()
                findAnswer()
                timerContinue = false
            }



        }.start()

        timerContinue = true
    }

    fun updateCountDownText(){

        val remainingTime : Int = (leftTime/1000).toInt()
        quizBinding.textViewTime.text = remainingTime.toString()
    }

    fun pauseTimer(){
        timer.cancel()
        timerContinue = false
    }

    fun resetTimer(){
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }

    // Finish button

    fun sendScore(){

        user?.let{

            val userUID = it.uid
            scoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect).addOnSuccessListener {

                Toast.makeText(applicationContext, "Score saved!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity, ResultActivity::class.java)
                startActivity(intent)
                finish()

            }

        }


    }
}