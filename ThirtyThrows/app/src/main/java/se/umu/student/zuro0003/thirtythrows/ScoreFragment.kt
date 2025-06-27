package se.umu.student.zuro0003.thirtythrows

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
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

        // Add rows of options in scoreboard from R.layout.score_row
        viewModel.scoreBoard.observe(viewLifecycleOwner) { scoreArray ->
            scoreArray.forEach { option ->
                val rowView = layoutInflater.inflate(R.layout.score_row, binding.scoreTable, false)

                val card = rowView.findViewById<MaterialCardView>(R.id.scoreRowCard)
                val titleText = rowView.findViewById<TextView>(R.id.titleText)
                val scoreText = rowView.findViewById<TextView>(R.id.scoreText)

                titleText.text = option.title
                scoreText.text = option.score.toString()

                if (option.locked) {
                    card.isClickable = false
                    card.alpha = 0.5f  // faded appearance
                } else {
                    card.setOnClickListener {
                        option.locked = true // lock the selected option
                        handleSelectedScore()
                    }
                }

                binding.scoreTable.addView(rowView)
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

    fun handleSelectedScore() {
        viewModel.nextRound()
        if (viewModel.isGameOver()) {
            findNavController().navigate(R.id.action_playFragment_to_gameOverFragment)
        }
        val result = Bundle().apply {
            putBoolean("userSelectedScore", true)
        }
        parentFragmentManager.setFragmentResult("scoreResult", result)
        dismiss() // Remove scoreboard dialog
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