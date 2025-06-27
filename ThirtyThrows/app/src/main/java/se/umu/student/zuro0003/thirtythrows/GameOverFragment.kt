package se.umu.student.zuro0003.thirtythrows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import se.umu.student.zuro0003.thirtythrows.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewModel: ThirtyThrowsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameOverBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scoreText.text = viewModel.getFinalScore().toString()
        binding.replayButton.setOnClickListener {
            viewModel.resetGame()
            findNavController().navigate(R.id.action_gameOverFragment_to_playFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // prevent memory leaks
    }
}