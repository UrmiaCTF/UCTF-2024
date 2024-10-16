<?php
// Get the HTTP Host header
// $host = $_SERVER['HTTP_HOST'];
    $userAgent = $_SERVER['HTTP_USER_AGENT'];
    $txt = '';
    // Check if the Host header contains <script></script> tag
    if (strpos($userAgent, '<script>') !== false || strpos($userAgent, '</script>') !== false) {
        $txt = 'good job\nFlag is\nuctf{Ir4n_Masal_County}';
        // $userAgent = htmlspecialchars($userAgent, ENT_QUOTES);
    }

    // Continue with normal processing if <script></script> tag is not found
    else{
        $txt = 'I log everything for admins\nso\nbe careful.';
    }
    
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>UCTF</title>
    <style>
        body {
            font-family: 'Courier New', Courier, monospace;
            background-color: #000;
            color: #0f0;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            text-align: center;
        }
        #text {
            font-size: 24px;
            white-space: pre-wrap;
        }
        footer {
            display: none;
            position: absolute;
            bottom: 20px;
            width: 50%;
            text-align: center;
            color: #0f0;
        }
    </style>
</head>
<body>
    <div id="text"></div>
    <audio id="tick-sound" src="assets/type.mp3" preload="auto"></audio>
    <footer id="footer"><p>your agent:</p><?php echo $userAgent; ?> </footer>

    <script>
        const textElement = document.getElementById('text');
        const tickSound = document.getElementById('tick-sound');
        const text = <?php echo '"'.$txt .'"';  ?>;
        const footerElement = document.getElementById('footer');

        let index = 0;

        function typeChar() {
            if (index < text.length) {
                const char = text.charAt(index);
                if (char === '\n') {
                    textElement.innerHTML += '<br>';
                } else {
                    textElement.innerHTML += char;
                }
                tickSound.play();
                index++;
                setTimeout(typeChar, 100); // Adjust the delay for typing speed
            } else {
                footerElement.style.display = 'block'; // Show the footer
            }
        }

        document.addEventListener('DOMContentLoaded', (event) => {
            typeChar();
        });
    </script>
</body>
</html>
