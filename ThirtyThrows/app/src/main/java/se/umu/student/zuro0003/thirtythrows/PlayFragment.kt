package se.umu.student.zuro0003.thirtythrows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import se.umu.student.zuro0003.thirtythrows.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewModel: ThirtyThrowsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dice = listOf(
            binding.die0,
            binding.die1,
            binding.die2,
            binding.die3,
            binding.die4,
            binding.die5
        )
        val diceTexture = listOf(
            R.drawable.die_1,
            R.drawable.die_2,
            R.drawable.die_3,
            R.drawable.die_4,
            R.drawable.die_5,
            R.drawable.die_6,
            R.drawable.gray_die_6,
            R.drawable.gray_die_5,
            R.drawable.gray_die_4,
            R.drawable.gray_die_3,
            R.drawable.gray_die_2,
            R.drawable.gray_die_1
        )

        viewModel.dices.observe(viewLifecycleOwner) { diceArray ->
            diceArray.forEachIndexed { index, value ->
                val texture = if (value > 0) value - 1 else value + 12
                dice[index].setImageResource(diceTexture[texture])
            }
        }
        for (i in dice.indices) {
            dice[i].setOnClickListener {
                viewModel.toggleDice(i)
            }
        }
        binding.diceRolls.text = getString(R.string.rolls_remaining, viewModel.rolls)
        binding.diceThrow.setOnClickListener {
            binding.diceRolls.text = getString(R.string.rolls_remaining, viewModel.rolls - 1)
            lifecycleScope.launch {
                binding.diceThrow.isEnabled = false
                viewModel.rollDice()
                binding.diceThrow.isEnabled = (viewModel.rolls > 0)
            }
        }
    }

}