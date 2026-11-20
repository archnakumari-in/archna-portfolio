const menuToggle = document.getElementById('menuToggle');
const nav = document.getElementById('nav');
menuToggle.addEventListener('click', () => nav.classList.toggle('open'));
nav.querySelectorAll('a').forEach(link => link.addEventListener('click', () => nav.classList.remove('open')));
document.getElementById('year').textContent = new Date().getFullYear();

const form = document.getElementById('contactForm');
const statusText = document.getElementById('formStatus');
form.addEventListener('submit', async (event) => {
  event.preventDefault();
  const button = form.querySelector('button[type="submit"]');
  button.disabled = true;
  button.textContent = 'Sending...';
  statusText.textContent = '';
  const payload = {
    name: form.name.value.trim(),
    email: form.email.value.trim(),
    message: form.message.value.trim()
  };
  try {
    const response = await fetch('/api/contact', {
      method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(payload)
    });
    const result = await response.json().catch(() => ({}));
    if (!response.ok) throw new Error(result.message || 'Please check your details and try again.');
    statusText.textContent = result.message || 'Thank you! Your message has been received.';
    form.reset();
  } catch (error) {
    statusText.textContent = error.message || 'Unable to send your message right now.';
    statusText.style.color = '#fca5a5';
  } finally {
    button.disabled = false;
    button.innerHTML = 'Send message <span>↗</span>';
  }
});
