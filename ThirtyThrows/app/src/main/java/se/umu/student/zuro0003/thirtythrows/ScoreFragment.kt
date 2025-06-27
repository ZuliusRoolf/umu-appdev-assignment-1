package se.umu.student.zuro0003.thirtythrows

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import se.umu.student.zuro0003.thirtythrows.databinding.FragmentScoreBinding

class ScoreFragment() : DialogFragment() {

    private var _binding: FragmentScoreBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val viewModel: ThirtyThrowsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentScoreBinding.inflate(requireActivity().layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.scoreBoard.observe(viewLifecycleOwner) { scoreArray ->
            scoreArray.forEach { option ->
                val row = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                val label = TextView(context).apply {
                    text = option.title
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                }

                val button = Button(context).apply {
                    text = option.score.toString()
                    isEnabled = (option.locked == false)
                    setOnClickListener {
                        option.locked = true
                        val result = Bundle().apply {
                            putBoolean("userSelectedScore", true)
                        }
                        parentFragmentManager.setFragmentResult("scoreResult", result)
                        dismiss()
                    }
                }

                row.addView(label)
                row.addView(button)
                binding.scoreTable.addView(row)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        val result = Bundle().apply {
            putBoolean("userSelectedScore", false)
        }
        parentFragmentManager.setFragmentResult("scoreResult", result)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // prevent memory leaks
    }

}