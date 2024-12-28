use std::fs;

fn main() {
    let file_path = "input11.txt";
    
    let content = fs::read_to_string(file_path).unwrap();
    let stones = content.lines().collect::<Vec<_>>()[0].split(" ").map(|s| s.parse().unwrap()).collect::<Vec<u64>>();

    const MAX: u8 = 75;

    let mut total = 0;
    for stone in stones {
        println!("Stone: {}", stone);
        let blink = blink(0, MAX, stone, 0);
        println!("Blink: {}", blink);
        total = total + blink;
        println!("Total: {}", total);
    }

    println!("{}", total);
}

fn blink(start: u8, max: u8, stone: u64, total: u64) -> u64 {
    // Base case
    if start == max {
        return total + 1;
    }

    // Blink
    let new_stones = calculate_stones(stone);

    // Recursion
    let mut partial = 0;
    for s in new_stones {
        partial = partial + blink(start + 1, max, s, total);
    }

    partial + total
}

fn calculate_stones(stone: u64) -> Vec<u64> {
    let mut new_stones = vec![];
    
    if stone == 0 { // Rules 1
        new_stones.push(1);
    } else if stone.to_string().len() % 2 == 0 { // Rules 2
        let stone_str = stone.to_string();
        let half = stone_str.len() / 2;
        new_stones.push((&stone_str[..half]).parse().unwrap());
        new_stones.push((&stone_str[half..]).parse().unwrap());
    } else { // Rules 3
        new_stones.push(stone * 2024);
    }

    new_stones
}