import random

SCORE_OPTIONS = ["<=3", "4  ", "5  ", "6  ", "7  ", "8  ", "9  ", "10 ", "11 ", "12 "]

def roll_dice(locked):
    return [random.randint(1, 6) if not locked[i] else dice[i] for i in range(6)]

def display_dice(dice, locked = [False] * 6):
    s = ""
    for i in range(6):
        lock_status = "|" if locked[i] else " "
        s += f" {lock_status}{dice[i]}{lock_status}"
    print('-'*(len(s)*7//6))
    print(s)
    print('-'*(len(s)*7//6))

def select_score(dice, score):
    s = ""
    sorted_dice = sorted(dice)
    for i in range(10):
        if i == 0:
            low = 0
            for value in sorted_dice:
                low += value if value <= 3 else 0
            s  += f"{SCORE_OPTIONS[i]} {low}\n"
            continue
        s  += f"{SCORE_OPTIONS[i]} {find_combinations(sorted_dice, i+3)}\n"
        s  += f"{SCORE_OPTIONS[i]} {count_score(sorted_dice, i+3)}\n"
    print(s)
    display_dice(sorted_dice)

# def count_score(dice, value):
#     d = sorted(dice, reverse=True)
#     print(d)
#     s = sum(d)
#     print(s)
#     score = (s//value)*value
#     print(score)
#     r = s%value
#     print(r)
#     if r == 0:
#         return score

def count_score(dice: list, value: int):
    combinations = []
    dice = sorted(dice, reverse=True)
    def find_combinations(dice: list, target, current_combination):
        if target == 0:
            combinations.append(current_combination)
            return
        if target < 0 or not dice:
            return
        
        # Include the first die
        find_combinations(dice[1:], target - dice[0], current_combination + [dice[0]])
        # Exclude the first die
        find_combinations(dice[1:], target, current_combination)

    find_combinations(dice, value, [])
    return combinations

def find_combinations(dice_values, target_sum):
    def backtrack(start, current_sum, current_combination):
        if current_sum == target_sum:
            result.append(list(current_combination))  # Found a valid combination
            return
        if current_sum > target_sum:
            return  # Prune the search if sum exceeds target
        
        for i in range(start, len(dice_values)):
            # Skip duplicate dice values
            if i > start and dice_values[i] == dice_values[i - 1]:
                continue
            # Add current dice to the combination and recurse
            current_combination.append(dice_values[i])
            backtrack(i + 1, current_sum + dice_values[i], current_combination)
            current_combination.pop()  # Backtrack and remove last added dice

    sorted(dice_values, reverse=True)  # Sort the dice values to help with pruning and skipping duplicates
    result = []
    backtrack(0, 0, [])
    return result

def my_count_score(dice, value):
    sorted_dice = sorted(dice, reverse=True)
    # TODO Create a working function that sorts the dice values, 
    # take the biggest value and then recursevly combine with smaller values to see if it reaches the target.
    # If the value is too low then add another number to the combination.
    # If the value is too high then go to the next number (lower number).
    # If the value is on target then remove said numbers from the list and rerun the function on the new set.
    # If there is no combinations then return the list of combinations.



def display_score(score):
    s = "Sum of:\n"
    sum = 0
    for i in range(10):
        saved_score = "-" 
        if score[i] != -1:
            saved_score = str(score[i])
            sum += score[i]
        s += f"{SCORE_OPTIONS[i]} {saved_score}\n"
    s += f"Total: {sum}"
    print(s)

def main():
    global dice
    dice = roll_dice([False] * 6)
    locked = [False] * 6
    
    score = [-1]*10

    rounds = 10
    throws = 3

    while throws > 0:
        print(f"Round {11-rounds}\nThrow: {4 - throws}")
        display_dice(dice, locked)
        user_input = input("Press '1'-'6' to toggle lock, or 'Enter' to reroll unlocked dice: ")

        if user_input == '':
            throws -= 1
            if throws <= 0: break
            dice = roll_dice(locked)

        elif user_input in '123456':
            index = int(user_input) - 1
            locked[index] = not locked[index]
    select_score(dice, score)

    
    

if __name__ == "__main__":
    main()