from flask import Flask, render_template, request, jsonify
import sqlite3
import random
import string


# Takhte_Soleyman_Ancient_Wonders
FLAG = "74kh73_5013ym4n_4nc13n7_w0nd325"


# Connect to the database (or create it if it doesn't exist)
def get_db_connection():
    conn = sqlite3.connect('attractions.db')
    conn.row_factory = sqlite3.Row  # To return rows as dictionaries
    return conn


# Function to drop all tables and migrate the new ones
def migrate():
    conn = get_db_connection()
    cur = conn.cursor()

    # Drop tables if they exist
    cur.execute("DROP TABLE IF EXISTS attractions")
    
    # Fetch all table names that start with 'flag_'
    cur.execute("SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'flag_%'")
    flag_tables = cur.fetchall()

    # Drop all tables that start with 'flag_'
    for table in flag_tables:
        cur.execute(f"DROP TABLE IF EXISTS {table[0]}")


    # Generate random table name for flag table
    random_chars = ''.join(random.choices(string.ascii_lowercase + string.digits, k=8))
    flag_table_name = f"flag_{random_chars}"

    # Create the flag table and insert the flag
    cur.execute(f"CREATE TABLE {flag_table_name} (flag TEXT NOT NULL)")
    cur.execute(f"INSERT INTO {flag_table_name} (flag) VALUES ('UCTF{{{FLAG}}}')")

    # Create the attractions table
    cur.execute('''
        CREATE TABLE attractions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            description TEXT NOT NULL,
            image_name TEXT NOT NULL,
            count INTEGER DEFAULT 0
        )
    ''')

    # Insert random attractions
    attractions = [
        ('Persepolis', 'An ancient ceremonial capital of the Achaemenid Empire, Persepolis is renowned for its impressive ruins, showcasing Persian architectural grandeur and historical significance.', 'Persepolis.jpg', 190),
        ('Naqsh-e Jahan Square', 'Located in Isfahan, this UNESCO World Heritage site is a vast public square surrounded by stunning examples of Islamic architecture, including mosques and palaces.', 'Naqsh_e_jahan.jpg', 147),
        ('Golestan Palace', 'A masterpiece of the Qajar era in Tehran, this historic palace complex blends Persian and European architecture, featuring ornate halls, gardens, and museums.', 'Golestan_Palace.jpg', 175),
        ('Masouleh', 'A picturesque village in northern Iran, Masouleh is famous for its unique terraced layout, where the rooftops of houses serve as courtyards for homes above them.', 'Masouleh.jpg', 130),
        ('Urmia Lake', 'Once one of the largest saltwater lakes in the world, Urmia Lake is known for its striking colors and ecological significance, although it has faced severe droughts in recent years.', 'Urmia_Lake.jpg', 120),
        ('Azadi Tower', 'A symbol of freedom in Tehran, Azadi Tower stands as an iconic monument, blending classical Persian architecture with contemporary design to celebrate freedom and history.', 'Azadi-Tower.jpg', 132)
    ]
    
    for attraction in attractions:
        cur.execute('''
            INSERT INTO attractions (name, description, image_name, count) 
            VALUES (?, ?, ?, ?)
        ''', attraction)

    # Commit changes and close the connection
    conn.commit()
    conn.close()

# Set the static_folder to 'assets' and the template_folder to 'Template'
app = Flask(__name__, static_folder='Template/assets', template_folder='Template')


# Route to serve the index.html file
@app.route('/')
def index():
    return render_template('index.html')


# API to vote for an attraction by id
@app.route('/vote', methods=['POST'])
def vote():
    data = request.json
    attraction_id = data.get('id')

    if not attraction_id:
        return jsonify({'error': 'Attraction ID is required'}), 400

    try:
        conn = get_db_connection()
        cur = conn.cursor()

        cur.execute('UPDATE attractions SET count = count + 1 WHERE id = ?', (attraction_id,))
        conn.commit()
        conn.close()

        if cur.rowcount == 0:
            return jsonify({'error': 'Attraction not found'}), 404

        return jsonify({'message': 'Vote counted successfully!'}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    
# API to list all attractions with POST and order in request body
@app.route('/attractions', methods=['POST'])
def list_attractions():
    data = request.json
    order = data.get('order', 'id')  # Default order by 'id' if not provided

    try:
        conn = get_db_connection()
        cur = conn.cursor()

        # Fetch attractions ordered by the given field
        query = f"SELECT * FROM attractions ORDER BY {order}"
        cur.execute(query)
        attractions = cur.fetchall()
        conn.close()

        # Convert rows to list of dictionaries
        attractions_list = [dict(row) for row in attractions]

        return jsonify(attractions_list), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    migrate()
    app.run(host="0.0.0.0", port=5000)