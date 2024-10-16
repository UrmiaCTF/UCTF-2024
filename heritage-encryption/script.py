#!/usr/bin/env python

from fuzzywuzzy import fuzz

# Flag for the challenge
challenge_flag = "UCTF{T1m3l3ss_W0nd3r5_0f_4nc13nt_P3rs14_F4ll3n_Gr34tN3ss}"

# List of places and clues for each stage
places = [
    {
        "name": "Takht-e Jamshīd",
        "clue": "The ancient throne of kings, where stone pillars whisper tales of imperial grandeur.",
    },
    {
        "name": "Chogha Zanbil",
        "clue": "A towering relic from the sands, where echoes of ancient gods and ziggurats still resonate.",
    },
    {
        "name": "Arg-e Bam",
        "clue": "A fortress of resilience amidst the desert, cradling centuries of forgotten secrets.",
    },
    {
        "name": "Behistun Inscription",
        "clue": "A monumental script carved in rock, narrating the epic victories of a king against a backdrop of rugged cliffs.",
    },
    {
        "name": "Shushtar",
        "clue": "An ancient city of water and stone, where ingenious engineering defied the forces of nature.",
    },
    {
        "name": "Qal’at-e Jargar",
        "clue": "A solitary fortress steeped in mystery, guarding the whispers of Persia’s ancient past.",
    },
    {
        "name": "Takht-e Soleyman",
        "clue": "A sacred sanctuary of fire and water, where divine mysteries and ancient rituals converge.",
    },
]


# Function to verify the answer and provide feedback
def verify_answer(stage, user_input, threshold=80):
    target = places[stage]["name"]

    # Calculate similarity score
    similarity_score = fuzz.ratio(user_input.lower(), target.lower())

    print(f"Your similarity score: {similarity_score:.2f}")

    if similarity_score >= threshold:
        print(f"Congratulations! You've completed stage {stage + 1}.")
        print(f"The correct answer for this stage was: {target}")
        if stage == len(places) - 1:
            print(f"Challenge Completed! Here is your flag: {challenge_flag}")
        return True
    else:
        print(f"Stage {stage + 1} failed. Try again!")
        return False


# Main challenge function
def run_challenge():
    print("Welcome to the 'Heritage Encryption: Cracking the Past' Challenge!")
    print("Solve the clues to reveal the ancient secrets of Persia.")
    print("You have only 90 seconds to finish the challange :)")

    for stage in range(len(places)):
        print(f"\nStage {stage + 1}:")
        print(f"Clue: {places[stage]['clue']}")

        while True:
            user_input = input("Enter your answer: ")
            if verify_answer(stage, user_input):
                break


if __name__ == "__main__":
    run_challenge()

