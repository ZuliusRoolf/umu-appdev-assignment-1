import random

SCORE_OPTIONS = ["low", "4  ", "5  ", "6  ", "7  ", "8  ", "9  ", "10 ", "11 ", "12 "]

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
    calculated_scores = [0] * 10
    print('='*12)
    for i in range(10):
        s = f"[{i}] " if score[i] == -1 else f"[-] "
        if i == 0:
            low = 0
            for value in sorted_dice:
                low += value if value <= 3 else 0
            s  += f"{SCORE_OPTIONS[i]} -> {low}"
            print(s)
            calculated_scores[i] = low
            continue
        calculated_scores[i] = len(find_combinations(sorted_dice, i+3)) * (i + 3)
        s  += f"{SCORE_OPTIONS[i]} -> {calculated_scores[i]}"
        print(s)
    while True:
        user_input = input("Press '0'-'9' to select which sum to use as score: ")
        if user_input.isdigit() and 0 <= int(user_input) < 10:
            index = int(user_input)
            if score[index] == -1:
                score[index] = calculated_scores[index]
                print(f"Sum of {SCORE_OPTIONS[index]} selected with score {calculated_scores[index]}.")
                return
            else:
                print(f"Sum of {SCORE_OPTIONS[index]} already scored.")
        else:
            print("Invalid input. Please enter a number between 0 and 9.")

def find_combinations(dice, target) -> list:
    sorted_dice = sorted(dice, reverse=True)
    combinations = []
    i = 0
    while i < len(sorted_dice):
        current_combination = [sorted_dice[i]]
        j = i + 1
        success = False
        # print(f"{sorted_dice}")
        # print(f"Checking combinations starting with {current_combination} at index {i}")
        while j < len(sorted_dice):
            if sum(current_combination) < target:
                # print(f"Current combination {current_combination} is less than target {target}.")
                # print(f"Add {sorted_dice[j]} from index {j} to current combination.")
                current_combination.append(sorted_dice[j])
            elif sum(current_combination) > target:
                # print(f"Current combination {current_combination} exceeds target {target}.")
                # print(f"Replace with {sorted_dice[j]} from index {j} to current combination.")
                current_combination.pop()
                current_combination.append(sorted_dice[j])
            if sum(current_combination) == target:
                combinations.append(current_combination)
                success = True
                # print(f"Found combination: {current_combination}")
                for num in current_combination:
                    sorted_dice.remove(num)
                break
            j += 1
        i += 1 if not success else 0
    
    return combinations


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
    score = [-1]*10
    rounds = 10
    while rounds > 0:
        throws = 3
        dice = roll_dice([False] * 6)
        locked = [False] * 6
        while throws > 0:
            print(f"Round {11-rounds}\nThrow: {4 - throws}")
            display_dice(dice, locked)
            user_input = input("'1'-'6' to toggle lock\n's' to display your scores\n'q' to quit\n'Enter' to reroll unlocked dice: ")

            if user_input == '':
                throws -= 1
                if throws <= 0: break
                dice = roll_dice(locked)

            elif user_input in '123456' and len(user_input) == 1:
                index = int(user_input) - 1
                locked[index] = not locked[index]

            elif user_input in 's' and len(user_input) == 1:
                display_score(score)

            elif user_input in 'q' and len(user_input) == 1:
                print("Exiting the game.")
                exit()

            else:
                print("Invalid input. Please enter a number between 1 and 6, or press Enter to reroll.")
                continue
        select_score(dice, score)
        rounds -= 1
    display_score(score)

if __name__ == "__main__":
    # dice = [1, 2, 3, 6, 6, 6]
    # r = find_combinations(dice, 12)
    # print("Result: ", r)
    main()