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

    private val game: ThirtyThrowsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleDiceUI()
        handleThrows()
        handleScoreSelection()
    }

    /** Binds dice textures to dice state and make each dice toggle when pressed */
    private fun handleDiceUI() {
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
        //        Convert dice value into texture
        game.dices.observe(viewLifecycleOwner) { diceArray ->
            diceArray.forEachIndexed { index, value ->
                val texture = if (value > 0) value - 1 else value + 12
                dice[index].setImageResource(diceTexture[texture])
            }
        }
        //        Handle dice presses to toggle dice lock
        for (i in dice.indices) {
            dice[i].setOnClickListener {
                game.toggleDice(i)
            }
        }
    }

    /** Updates Round and Rolls count.
     * Determines whether the throw button will roll dice, display score board or go to result screen*/
    private fun handleThrows() {
        // Update UI to current game round and dice rolls
        binding.diceRound.text = getString(R.string.round_count, game.round)
        binding.diceRolls.text = getString(R.string.rolls_remaining, game.rolls)
        binding.diceThrow.text =
            if (game.rolls > 0) getString(R.string.dice_throw) else getString(R.string.select_score)
        val dialog = ScoreFragment()
        binding.diceThrow.setOnClickListener {
            // Roll the die
            if (game.rolls > 0) {
                binding.diceRolls.text = getString(R.string.rolls_remaining, game.rolls - 1)
                // Disable button during roll "animation" and show ScoreDialogFragment if last roll
                lifecycleScope.launch {
                    binding.diceThrow.isEnabled = false
                    game.rollDice()
                    binding.diceThrow.isEnabled = game.rolls > 0
                    if (game.rolls == 0) {
                        game.updateScoreBoard()
                        dialog.show(parentFragmentManager, "score_selector")
                    }
                }
            } else {
                // If the user accidentally backed out of scoreboard dialog, let them open it again
                dialog.show(parentFragmentManager, "score_selector")
                binding.diceThrow.isEnabled = false
            }

        }
    }

    /** Update UI to select score if user dismisses the scoreboard */
    private fun handleScoreSelection() {
        parentFragmentManager.setFragmentResultListener(
            "scoreResult", viewLifecycleOwner
        ) { _, bundle ->
            binding.diceThrow.isEnabled = true
            if (bundle.getBoolean("userSelectedScore")) {
                binding.diceThrow.text = getString(R.string.dice_throw)
                binding.diceRolls.text = getString(R.string.rolls_remaining, game.rolls)
                binding.diceRound.text = getString(R.string.round_count, game.round)
            } else {
                binding.diceThrow.text = getString(R.string.select_score)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // prevent memory leaks
    }

}