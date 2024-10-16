from flask import Flask, render_template, request, redirect, url_for
import os

# Set the static_folder to 'assets' and the template_folder to 'Template'
app = Flask(__name__, static_folder='Template/assets', template_folder='Template')



# Route to serve the index.html file
@app.route('/')
def index():
    return render_template('index.html')

# Route to handle POST requests for saving full name and email
@app.route('/submit', methods=['POST'])
def submit():
    
    data = request.get_json()
    full_name = data.get('full_name')
    email = data.get('email')

    print(full_name)
    print(email)

    # Ensure both fields are provided
    if full_name and email:
        # Save the data to a file
        with open('submissions.txt', 'a') as f:
            f.write(f"Full Name: {full_name}, Email: {email}\n")
        return redirect(url_for('index'))  # Redirect to the home page
    else:
        return "Please provide both full name and email.", 400

if __name__ == '__main__':
    app.run(debug=True)
